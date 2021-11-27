package intcode

import org.slf4j.LoggerFactory
import java.io.File

class IntcodeComputer(originalProgram: List<Long>, private val inputs: MutableList<Long> = mutableListOf()) {

    private var instructionPointer = 0L
    private var valuesInInstruction = 0L
    private var relativeBase = 0L
    private val output = mutableListOf<Long>()
    private val logger = LoggerFactory.getLogger(IntcodeComputer::class.java)
    private var inputProvider: IntcodeInputProvider? = null

    var ended = false
    private val program = originalProgram
        .mapIndexed{i, v -> i.toLong() to v}
        .toMap()
        .toMutableMap()
        .withDefault { 0L }

    val state
        get() = program.values.toList()

    fun calculateIntcode(): List<Long> {
        while (!ended) {
            val instruction = program.getValue(instructionPointer)
            val (parameterModes, opcode) = readInstruction(instruction)
            logger.debug("processing opcode {}", opcode)
            valuesInInstruction = when (opcode) {
                1L -> add(parameterModes)
                2L -> multiply(parameterModes)
                3L -> input(parameterModes)
                4L -> output(parameterModes)
                5L -> jumpIfTrue(parameterModes)
                6L -> jumpIfFalse(parameterModes)
                7L -> lessThan(parameterModes)
                8L -> isEqual(parameterModes)
                9L -> adjustRelativeBase(parameterModes)
                99L -> {
                    ended = true; 0
                }
                else -> throw IllegalArgumentException("Unknown value $opcode, pointer: $instructionPointer")
            }

            logger.debug("moving instruction pointer to {}", instructionPointer + valuesInInstruction)
            instructionPointer += valuesInInstruction
        }

        return output
    }

    fun getOutput(): Long {
        return output.last()
    }

    fun getAllOutput(): List<Long> {
        return output
    }

    fun supplyInput(input: Long) {
        inputs.add(input)
    }

    fun registerInputProvider(inputProvider: IntcodeInputProvider) {
        this.inputProvider = inputProvider
    }

    private fun add(parameterModes: List<ParameterMode>): Long {
        val param1 = program.getValue(getParamValue(1, 0, parameterModes))
        val param2 = program.getValue(getParamValue(2, 1, parameterModes))
        val param3 = getParamValue(3, 2, parameterModes)

        val result = param1 + param2
        logger.debug("Setting #{} to {}", param3, result)
        program[param3] = result

        return 4
    }

    private fun multiply(parameterModes: List<ParameterMode>): Long {
        val param1 = program.getValue(getParamValue(1, 0, parameterModes))
        val param2 = program.getValue(getParamValue(2, 1, parameterModes))
        val param3 = getParamValue(3, 2, parameterModes)

        val result = param1 * param2
        logger.debug("setting #{} to {}", param3, result)
        program[param3] = result

        return 4
    }

    private fun input(parameterModes: List<ParameterMode>): Long {
        if (inputs.isEmpty()) {
            inputProvider?.let { supplyInput(it.getNext(this)) }
        }
        val input = inputs.removeAt(0)
        val param1 = getParamValue(1, 0, parameterModes)

        logger.debug("setting #{} to input {}", param1, input)
        program[param1] = input

        return 2
    }

    private fun output(parameterModes: List<ParameterMode>): Long {
        val param1 = getParamValue(1, 0, parameterModes)
        logger.debug("outputting value at #{}", param1)
        output += program.getValue(param1)

        return 2
    }

    private fun jumpIfTrue(parameterModes: List<ParameterMode>): Long {
        val param1 = getParamValue(1, 0, parameterModes)
        val param2 = getParamValue(2, 1, parameterModes)

        logger.debug("checking if #{} is not 0 [it is {}]", param1, program[param1])
        return if (program[param1] != 0L) {
            program.getValue(param2) - instructionPointer
        } else 3
    }

    private fun jumpIfFalse(parameterModes: List<ParameterMode>): Long {
        val param1 = getParamValue(1, 0, parameterModes)
        val param2 = getParamValue(2, 1, parameterModes)

        logger.debug("checking if #{} is 0 [it is {}]", param1, program[param1])
        return if (program[param1] == 0L) {
            (program.getValue(param2) - instructionPointer)
        } else 3
    }

    private fun lessThan(parameterModes: List<ParameterMode>): Long {
        val param1 = getParamValue(1, 0, parameterModes)
        val param2 = getParamValue(2, 1, parameterModes)
        val param3 = getParamValue(3, 2, parameterModes)

        val result = if (program.getValue(param1) < program.getValue(param2)) 1L else 0L
        logger.debug("setting #{} to {}", param3, result)
        program[param3] = result

        return 4
    }

    private fun isEqual(parameterModes: List<ParameterMode>): Long {
        val param1 = getParamValue(1, 0, parameterModes)
        val param2 = getParamValue(2, 1, parameterModes)
        val param3 = getParamValue(3, 2, parameterModes)

        val result = if (program[param1] == program.getValue(param2)) 1L else 0L
        logger.debug("setting #{} to {}", param3, result)
        program[param3] = result

        return 4
    }

    private fun adjustRelativeBase(parameterModes: List<ParameterMode>): Long {
        val param1 = getParamValue(1, 0, parameterModes)
        logger.debug("adjusting relative base by {}", program.getValue(param1))
        relativeBase += program.getValue(param1)

        return 2
    }

    fun readInstruction(instruction: Long): Pair<List<ParameterMode>, Long> {
        return if (instruction < 100) {
            Pair(listOf(ParameterMode.POSITION), instruction)
        } else {
            val instStr = instruction.toString()
            val opcode = instStr.substring(instStr.length - 2).toLong()
            val parameterModes = instStr.substring(0, instStr.length - 2)
                .map { ParameterMode.fromMode(it.toString().toLong()) }
                .reversed()
            Pair(parameterModes, opcode)
        }
    }

    private fun getParamValue(param: Long, argNum: Long, parameterModes: List<ParameterMode>): Long {
        val mode = parameterModes.getOrElse(argNum.toInt()) { ParameterMode.POSITION }
        val index = param + instructionPointer
        return when(mode) {
            ParameterMode.POSITION -> program.getValue(index)
            ParameterMode.IMMEDIATE -> index
            ParameterMode.RELATIVE -> program.getValue(index) + relativeBase
        }
    }

    enum class ParameterMode(private val mode: Long) {
        POSITION(0),
        IMMEDIATE(1),
        RELATIVE(2);

        companion object {
            fun fromMode(mode: Long): ParameterMode {
                return values().first { it.mode == mode }
            }
        }
    }

    companion object {
        fun readInput(inputPath: String): List<Long> = File(inputPath)
            .readText()
            .trim()
            .split(",")
            .map { it.toLong() }
    }
}

interface IntcodeInputProvider {
    fun getNext(intcodeComputer: IntcodeComputer): Long
}
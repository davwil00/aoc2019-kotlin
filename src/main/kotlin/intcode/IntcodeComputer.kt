package intcode

import java.io.File

class IntcodeComputer(originalProgram: List<Long>, private val inputs: MutableList<Long> = mutableListOf()) {

    private var instructionPointer = 0
    private var valuesInInstruction = 0
    private var relativeBase = 0L
    private val output = mutableListOf<Long>()
    var ended = false
    val program = Program(originalProgram)

    fun calculateIntcode(): List<Long> {
        while (!ended) {
            val instruction = program[instructionPointer]
            val (parameterModes, opcode) = readInstruction(instruction)

            valuesInInstruction = when (opcode) {
                1 -> add(parameterModes)
                2 -> multiply(parameterModes)
                3 -> input(inputs.removeAt(0), parameterModes)
                4 -> output(parameterModes)
                5 -> jumpIfTrue(parameterModes)
                6 -> jumpIfFalse(parameterModes)
                7 -> lessThan(parameterModes)
                8 -> isEqual(parameterModes)
                9 -> adjustRelativeBase(parameterModes)
                99 -> {
                    ended = true; 0
                }
                else -> throw IllegalArgumentException("Unknown value $opcode, pointer: $instructionPointer")
            }

            instructionPointer += valuesInInstruction
        }

        return output
    }

    fun getOutput(): Long {
        return output.last()
    }

    fun supplyInput(input: Long) {
        inputs.add(input)
    }

    private fun add(parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(program[1 + instructionPointer], 0, parameterModes)
        val param2 = getParamValue(program[2 + instructionPointer], 1, parameterModes)
        val param3 = getParamValue(3L + instructionPointer, 2, parameterModes).toInt()

        val result = param1 + param2
        program[param3] = result

        return 4
    }

    private fun multiply(parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(program[1 + instructionPointer], 0, parameterModes)
        val param2 = getParamValue(program[2 + instructionPointer], 1, parameterModes)
        val param3 = getParamValue(3L + instructionPointer, 2, parameterModes).toInt()

        val result = param1 * param2
        program[param3] = result

        return 4
    }

    private fun input(input: Long, parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(1L + instructionPointer, 0, parameterModes).toInt()

        program[param1] = input

        return 2
    }

    private fun output(parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(1L + instructionPointer, 0, parameterModes).toInt()
        output += program[param1]

        return 2
    }

    private fun jumpIfTrue(parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(1L + instructionPointer, 0, parameterModes).toInt()
        val param2 = getParamValue(2L + instructionPointer, 1, parameterModes).toInt()

        return if (program[param1] != 0L) {
            (program[param2] - instructionPointer).toInt()
        } else 3
    }

    private fun jumpIfFalse(parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(1L + instructionPointer, 0, parameterModes).toInt()
        val param2 = getParamValue(2L + instructionPointer, 1, parameterModes).toInt()

        return if (program[param1] == 0L) {
            (program[param2] - instructionPointer).toInt()
        } else 3
    }

    private fun lessThan(parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(1L + instructionPointer, 0, parameterModes).toInt()
        val param2 = getParamValue(2L + instructionPointer, 1, parameterModes).toInt()
        val param3 = getParamValue(3L + instructionPointer, 2, parameterModes).toInt()

        program[param3] = if (program[param1] < program[param2]) 1 else 0

        return 4
    }

    private fun isEqual(parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(1L + instructionPointer, 0, parameterModes).toInt()
        val param2 = getParamValue(2L + instructionPointer, 1, parameterModes).toInt()
        val param3 = getParamValue(3L + instructionPointer, 2, parameterModes).toInt()

        program[param3] = if (program[param1] == program[param2]) 1 else 0

        return 4
    }

    private fun adjustRelativeBase(parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(0L, 0, parameterModes).toInt()

        relativeBase += program[param1]

        return 2
    }

    private fun readInstruction(instruction: Long): Pair<List<ParameterMode>, Int> {
        return if (instruction < 100) {
            Pair(listOf(ParameterMode.POSITION), instruction.toInt())
        } else {
            val instStr = instruction.toString()
            val opcode = instStr.substring(2).toInt()
            val parameterModes = instStr.substring(0, instStr.length - 2)
                .map { ParameterMode.fromMode(it.toString().toInt()) }
                .reversed()
            Pair(parameterModes, opcode)
        }
    }

    private fun getParamValue(param: Long, argNum: Int, parameterModes: List<ParameterMode>): Long {
        val mode = parameterModes.getOrElse(argNum) { _ -> ParameterMode.POSITION}
        return when(mode) {
            ParameterMode.POSITION -> program[param.toInt()]
            ParameterMode.IMMEDIATE -> param
            ParameterMode.RELATIVE -> program[param.toInt()] + relativeBase
        }
    }

    enum class ParameterMode(private val mode: Int) {
        POSITION(0),
        IMMEDIATE(1),
        RELATIVE(2);

        companion object {
            fun fromMode(mode: Int): ParameterMode {
                return values().first { it.mode == mode }
            }
        }
    }

    companion object {
        fun readInput(inputPath: String): List<Long> = File(inputPath)
            .readText()
            .split(",")
            .map { it.toLong() }
    }

    class Program(contents: List<Long>): ArrayList<Long>(contents) {
        override fun get(index: Int): Long {
            while (index > size - 1) {
                add(0)
            }
            return super.get(index)
        }

        override fun set(index: Int, element: Long): Long {
            while (index > size - 1) {
                add(0)
            }
            return super.set(index, element)
        }
    }
}
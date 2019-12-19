package intcode

import java.io.File
import java.lang.IllegalArgumentException

class IntcodeComputer(private val program: MutableList<Int>, private val inputs: MutableList<Int> = mutableListOf()) {

    private var instructionPointer = 0
    private var valuesInInstruction: Int = 0
    private val output = mutableListOf<Int>()
    var ended = false

    fun calculateIntcode(): List<Int> {
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
                99 -> { ended = true; 0 }
                else -> throw IllegalArgumentException("Unknown value $opcode, pointer: $instructionPointer")
            }

            instructionPointer += valuesInInstruction
        }

        return output
    }

    fun getOutput(): Int {
        return output.last()
    }

    fun supplyInput(input: Int) {
        inputs.add(input)
    }

    private fun add(parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(program, program[1 + instructionPointer], 0, parameterModes)
        val param2 = getParamValue(program, program[2 + instructionPointer], 1, parameterModes)
        val param3 = getParamValue(program, 3 + instructionPointer, 2, parameterModes)

        val result = param1 + param2
        program[param3] = result

        return 4
    }

    private fun multiply(parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(program, program[1 + instructionPointer], 0, parameterModes)
        val param2 = getParamValue(program, program[2 + instructionPointer], 1, parameterModes)
        val param3 = getParamValue(program, 3 + instructionPointer, 2, parameterModes)

        val result = param1 * param2
        program[param3] = result

        return 4
    }

    private fun input(input: Int, parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(program, 1 + instructionPointer, 0, parameterModes)

        program[param1] = input

        return 2
    }

    private fun output(parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(program, 1 + instructionPointer, 0, parameterModes)
        output += program[param1]

        return 2
    }

    private fun jumpIfTrue(parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(program, 1 + instructionPointer, 0, parameterModes)
        val param2 = getParamValue(program, 2 + instructionPointer, 1, parameterModes)

        return if (program[param1] != 0) {
            program[param2] - instructionPointer
        } else 3
    }

    private fun jumpIfFalse(parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(program, 1 + instructionPointer, 0, parameterModes)
        val param2 = getParamValue(program, 2 + instructionPointer, 1, parameterModes)

        return if (program[param1] == 0) {
            program[param2] - instructionPointer
        } else 3
    }

    private fun lessThan(parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(program, 1 + instructionPointer, 0, parameterModes)
        val param2 = getParamValue(program, 2 + instructionPointer, 1, parameterModes)
        val param3 = getParamValue(program, 3 + instructionPointer, 2, parameterModes)

        program[param3] = if (program[param1] < program[param2]) 1 else 0

        return 4
    }

    private fun isEqual(parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(program, 1 + instructionPointer, 0, parameterModes)
        val param2 = getParamValue(program, 2 + instructionPointer, 1, parameterModes)
        val param3 = getParamValue(program, 3 + instructionPointer, 2, parameterModes)

        program[param3] = if (program[param1] == program[param2]) 1 else 0

        return 4
    }

    private fun readInstruction(instruction: Int): Pair<List<ParameterMode>, Int> {
        return if (instruction < 100) {
            Pair(listOf(ParameterMode.POSITION), instruction)
        } else {
            val instStr = instruction.toString()
            val opcode = instStr.substring(2).toInt()
            val parameterModes = instStr.substring(0, instStr.length - 2)
                .toCharArray()
                .map { ParameterMode.fromMode(it.toString().toInt()) }
                .reversed()
            Pair(parameterModes, opcode)
        }
    }

    private fun getParamValue(sequence: MutableList<Int>, param: Int, argNum: Int, parameterModes: List<ParameterMode>): Int {
        val mode = parameterModes.getOrElse(argNum) { _ -> ParameterMode.POSITION}
        return when(mode) {
            ParameterMode.POSITION -> sequence[param]
            ParameterMode.IMMEDIATE -> param
        }
    }

    enum class ParameterMode(private val mode: Int) {
        POSITION(0),
        IMMEDIATE(1);

        companion object {
            fun fromMode(mode: Int): ParameterMode {
                return values().first { it.mode == mode }
            }
        }
    }

    companion object {
        fun readInput(inputPath: String): List<Int> = File(inputPath)
            .readText()
            .split(",")
            .map { it.toInt() }
    }
}
package intcode

import java.io.File
import java.lang.IllegalArgumentException

class IntcodeComputer() {

    val output = mutableListOf<Int>()

    internal fun calculateIntcode(memory: MutableList<Int>, input: Int? = null): List<Int> {
        var instructionPointer = 0
        var valuesInInstruction: Int
        var ended = false

        while (!ended) {
            val instruction = memory[instructionPointer]
            val (parameterModes, opcode) = readInstruction(instruction)
            valuesInInstruction = when (opcode) {
                1 -> add(memory, instructionPointer, parameterModes)
                2 -> multiply(memory, instructionPointer, parameterModes)
                3 -> input(memory, instructionPointer, input!!, parameterModes)
                4 -> output(memory, instructionPointer, parameterModes)
                5 -> jumpIfTrue(memory, instructionPointer, parameterModes)
                6 -> jumpIfFalse(memory, instructionPointer, parameterModes)
                7 -> lessThan(memory, instructionPointer, parameterModes)
                8 -> equals(memory, instructionPointer, parameterModes)
                99 -> { ended = true; 0 }
                else -> throw IllegalArgumentException("Unknown value $opcode, pointer: $instructionPointer")
            }

            instructionPointer += valuesInInstruction
        }

        return memory
    }

    fun add(sequence: MutableList<Int>, offset: Int, parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(sequence, sequence[1 + offset], 0, parameterModes)
        val param2 = getParamValue(sequence, sequence[2 + offset], 1, parameterModes)
        val param3 = getParamValue(sequence, 3 + offset, 2, parameterModes)

        val result = param1 + param2
        sequence[param3] = result

        return 4
    }

    fun multiply(sequence: MutableList<Int>, offset: Int, parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(sequence, sequence[1 + offset], 0, parameterModes)
        val param2 = getParamValue(sequence, sequence[2 + offset], 1, parameterModes)
        val param3 = getParamValue(sequence, 3 + offset, 2, parameterModes)

        val result = param1 * param2
        sequence[param3] = result

        return 4
    }

    fun input(sequence: MutableList<Int>, offset: Int, input: Int, parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(sequence, 1 + offset, 0, parameterModes)

        sequence[param1] = input

        return 2
    }

    fun output(sequence: MutableList<Int>, offset: Int, parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(sequence, 1 + offset, 0, parameterModes)
        output += sequence[param1]

        return 2
    }

    fun jumpIfTrue(sequence: MutableList<Int>, offset: Int, parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(sequence, 1 + offset, 0, parameterModes)
        val param2 = getParamValue(sequence, 2 + offset, 1, parameterModes)

        return if (sequence[param1] != 0) {
            sequence[param2] - offset
        } else 3
    }

    fun jumpIfFalse(sequence: MutableList<Int>, offset: Int, parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(sequence, 1 + offset, 0, parameterModes)
        val param2 = getParamValue(sequence, 2 + offset, 1, parameterModes)

        return if (sequence[param1] == 0) {
            sequence[param2] - offset
        } else 3
    }

    fun lessThan(sequence: MutableList<Int>, offset: Int, parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(sequence, 1 + offset, 0, parameterModes)
        val param2 = getParamValue(sequence, 2 + offset, 1, parameterModes)
        val param3 = getParamValue(sequence, 3 + offset, 2, parameterModes)

        sequence[param3] = if (sequence[param1] < sequence[param2]) 1 else 0

        return 4
    }

    fun equals(sequence: MutableList<Int>, offset: Int, parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(sequence, 1 + offset, 0, parameterModes)
        val param2 = getParamValue(sequence, 2 + offset, 1, parameterModes)
        val param3 = getParamValue(sequence, 3 + offset, 2, parameterModes)

        sequence[param3] = if (sequence[param1] == sequence[param2]) 1 else 0

        return 4
    }

    fun readInput(inputPath: String): List<Int> = File(inputPath)
        .readText()
        .split(",")
        .map { it.toInt() }

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
}
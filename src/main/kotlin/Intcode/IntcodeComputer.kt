package Intcode

import java.io.File
import java.lang.IllegalArgumentException

class IntcodeComputer {

    internal fun calculateIntcode(memory: MutableList<Int>, input: Int? = null): List<Int> {
        var instructionPointer = 0
        var valuesInInstruction: Int
        var ended = false

        while (!ended) {
            val instruction = memory[instructionPointer]
            val (parameterModes, opcode) = readInstruction(instruction)
            valuesInInstruction = when (opcode) {
                1 -> opcode1(memory, instructionPointer, parameterModes)
                2 -> opcode2(memory, instructionPointer, parameterModes)
                3 -> opcode3(memory, instructionPointer, input!!, parameterModes)
                4 -> opcode4(memory, instructionPointer, parameterModes)
                99 -> { ended = true; 0 }
                else -> throw IllegalArgumentException("Unknown value $opcode, pointer: $instructionPointer")
            }

            instructionPointer += valuesInInstruction
        }

        return memory
    }

    fun opcode1(sequence: MutableList<Int>, offset: Int, parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(sequence, sequence[1 + offset], 0, parameterModes)
        val param2 = getParamValue(sequence, sequence[2 + offset], 1, parameterModes)
        val param3 = getParamValue(sequence, 3 + offset, 2, parameterModes)

        val result = param1 + param2
        sequence[param3] = result

        return 4
    }

    fun opcode2(sequence: MutableList<Int>, offset: Int, parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(sequence, sequence[1 + offset], 0, parameterModes)
        val param2 = getParamValue(sequence, sequence[2 + offset], 1, parameterModes)
        val param3 = getParamValue(sequence, 3 + offset, 2, parameterModes)

        val result = param1 * param2
        sequence[param3] = result

        return 4
    }

    fun opcode3(sequence: MutableList<Int>, offset: Int, input: Int, parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(sequence, 1 + offset, 0, parameterModes)

        sequence[param1] = input

        return 2
    }

    fun opcode4(sequence: MutableList<Int>, offset: Int, parameterModes: List<ParameterMode>): Int {
        val param1 = getParamValue(sequence, 1 + offset, 0, parameterModes)
        println("Output is ${sequence[param1]}")

        return 2
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
package day02

import java.io.File

class IntcodeCalculator {

    fun completeGravityAssist(target: Int): Pair<Int, Int> {
        for (noun in 0..99) {
            for (verb in 0..99) {
                val answer = restoreGravityAssist(noun, verb)
                if (answer == target) {
                    return Pair(noun, verb)
                }
            }
        }

        throw RuntimeException("Answer not found")
    }

    fun restoreGravityAssist(noun: Int, verb: Int): Int {
        val memory = readInput().toMutableList()
        memory[1] = noun
        memory[2] = verb

        val result = calculateIntcode(memory)
        return result[0]
    }

    internal fun calculateIntcode(memory: MutableList<Int>): List<Int> {
        var instructionPointer = 0
        val valuesInInstruction = 4
        var ended = false

        while (!ended) {
            when (memory[instructionPointer]) {
                1 -> opcode1(memory, instructionPointer)
                2 -> opcode2(memory, instructionPointer)
                99 -> ended = true
            }

            instructionPointer += valuesInInstruction
        }

        return memory
    }

    fun opcode1(sequence: MutableList<Int>, offset: Int) {
        val address1 = sequence[1 + offset]
        val address2 = sequence[2 + offset]
        val address3 = sequence[3 + offset]

        val result = sequence[address1] + sequence[address2]
        sequence[address3] = result
    }

    fun opcode2(sequence: MutableList<Int>, offset: Int) {
        val address1 = sequence[1 + offset]
        val address2 = sequence[2 + offset]
        val address3 = sequence[3 + offset]

        val result = sequence[address1] * sequence[address2]
        sequence[address3] = result
    }


    private fun readInput(): List<Int> = File("src/main/resources/day02/input.txt")
        .readText()
        .split(",")
        .map { it.toInt() }
}

fun main() {
    val intcodeCalculator = IntcodeCalculator()
    println(intcodeCalculator.restoreGravityAssist(12, 2))
    val (noun, verb) = intcodeCalculator.completeGravityAssist(19690720)
    println("noun = $noun, verb = $verb, answer = ${100 * noun + verb}")
}
package day02

import Intcode.IntcodeComputer

class GravityAssist {

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
        val intcodeComputer = IntcodeComputer()
        val memory = intcodeComputer.readInput("src/main/resources/day02/input.txt").toMutableList()
        memory[1] = noun
        memory[2] = verb

        val result = intcodeComputer.calculateIntcode(memory)
        return result[0]
    }
}

fun main() {
    val intcodeCalculator = GravityAssist()
    println(intcodeCalculator.restoreGravityAssist(12, 2))
    val (noun, verb) = intcodeCalculator.completeGravityAssist(19690720)
    println("noun = $noun, verb = $verb, answer = ${100 * noun + verb}")
}
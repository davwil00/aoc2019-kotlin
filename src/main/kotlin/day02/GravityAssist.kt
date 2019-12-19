package day02

import intcode.IntcodeComputer

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
        val program = IntcodeComputer.readInput("src/main/resources/day02/input.txt").toMutableList()
        val intcodeComputer = IntcodeComputer(program)
        program[1] = noun
        program[2] = verb

        val result = intcodeComputer.calculateIntcode()
        return result[0]
    }
}

fun main() {
    val gravityAssist = GravityAssist()
    println(gravityAssist.restoreGravityAssist(12, 2))
    val (noun, verb) = gravityAssist.completeGravityAssist(19690720)
    println("noun = $noun, verb = $verb, answer = ${100 * noun + verb}")
}
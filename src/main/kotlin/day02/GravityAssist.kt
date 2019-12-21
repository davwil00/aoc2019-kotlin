package day02

import intcode.IntcodeComputer

class GravityAssist {

    fun completeGravityAssist(target: Long): Pair<Long, Long> {
        for (noun in 0..99L) {
            for (verb in 0..99L) {
                val answer = restoreGravityAssist(noun, verb)
                if (answer == target) {
                    return Pair(noun, verb)
                }
            }
        }

        throw RuntimeException("Answer not found")
    }

    fun restoreGravityAssist(noun: Long, verb: Long): Long {
        val program = IntcodeComputer.readInput("src/main/resources/day02/input.txt").toMutableList()
        program[1] = noun
        program[2] = verb
        val intcodeComputer = IntcodeComputer(program)

        intcodeComputer.calculateIntcode()
        return intcodeComputer.state[0]
    }
}

fun main() {
    val gravityAssist = GravityAssist()
    println(gravityAssist.restoreGravityAssist(12, 2))
    val (noun, verb) = gravityAssist.completeGravityAssist(19690720)
    println("noun = $noun, verb = $verb, answer = ${100 * noun + verb}")
}
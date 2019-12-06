package day02

import java.io.File

class IntcodeCalculator {

    fun restoreGravityAssist(): Int {
        val program = readInput().toMutableList()
        program[1] = 12
        program[2] = 2

        val result = calculateIntcode(program)
        return result[0]
    }

    internal fun calculateIntcode(program: MutableList<Int>): List<Int> {
        var currentOffset = 0
        var ended = false

        while (!ended) {
            when (program[currentOffset]) {
                1 -> opcode1(program, currentOffset)
                2 -> opcode2(program, currentOffset)
                99 -> ended = true
            }

            currentOffset += 4
        }

        return program
    }

    fun opcode1(sequence: MutableList<Int>, offset: Int) {
        val pos1 = sequence[1 + offset]
        val pos2 = sequence[2 + offset]
        val pos3 = sequence[3 + offset]

        val result = sequence[pos1] + sequence[pos2]
        sequence[pos3] = result
    }

    fun opcode2(sequence: MutableList<Int>, offset: Int) {
        val pos1 = sequence[1 + offset]
        val pos2 = sequence[2 + offset]
        val pos3 = sequence[3 + offset]

        val result = sequence[pos1] * sequence[pos2]
        sequence[pos3] = result
    }


    private fun readInput(): List<Int> = File("src/main/resources/day02/input.txt")
        .readText()
        .split(",")
        .map { it.toInt() }
}

fun main() {
    val intcodeCalculator = IntcodeCalculator()
    println(intcodeCalculator.restoreGravityAssist())

}
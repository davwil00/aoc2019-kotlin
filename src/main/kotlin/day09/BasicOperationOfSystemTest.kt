package day09

import intcode.IntcodeComputer

class BasicOperationOfSystemTest {

    fun runDiagnostic(program: MutableList<Long>) {
        val intcodeComputer = IntcodeComputer(program, mutableListOf(1))
        val output = intcodeComputer.calculateIntcode()
        println(output)
    }
}

fun main() {
    val program = IntcodeComputer.readInput("src/main/resources/day09/input.txt")
    BasicOperationOfSystemTest().runDiagnostic(program.toMutableList())
}
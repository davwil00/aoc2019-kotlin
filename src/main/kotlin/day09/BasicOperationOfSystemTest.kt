package day09

import intcode.IntcodeComputer

class BasicOperationOfSystemTest {

    fun runDiagnostic(program: List<Long>) {
        val intcodeComputer = IntcodeComputer(program, mutableListOf(1))
        intcodeComputer.calculateIntcode()
        println(intcodeComputer.getOutput())
    }

    fun boostSensors(program: List<Long>) {
        val intcodeComputer = IntcodeComputer(program, mutableListOf(2))
        intcodeComputer.calculateIntcode()
        println(intcodeComputer.getOutput())
    }
}

fun main() {
    val program = IntcodeComputer.readInput("src/main/resources/day09/input.txt")
    val boost = BasicOperationOfSystemTest()
    boost.runDiagnostic(program)
    boost.boostSensors(program)
}
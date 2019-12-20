package day05

import intcode.IntcodeComputer

class ThermalEnvironmentSupervisionTerminal {

    fun runTEST(input: Long) {
        val program = IntcodeComputer.readInput("src/main/resources/day05/input.txt")
        val intcodeComputer = IntcodeComputer(program.toMutableList(), mutableListOf(input))
        val output = intcodeComputer.calculateIntcode()
        println(output)
    }
}

fun main() {
    ThermalEnvironmentSupervisionTerminal().runTEST(1)
    ThermalEnvironmentSupervisionTerminal().runTEST(5)
}
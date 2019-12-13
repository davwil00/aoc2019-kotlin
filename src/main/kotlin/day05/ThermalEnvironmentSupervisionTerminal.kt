package day05

import intcode.IntcodeComputer

class ThermalEnvironmentSupervisionTerminal {

    fun runTEST(input: Int) {
        val intcodeComputer = IntcodeComputer()
        val memory = intcodeComputer.readInput("src/main/resources/day05/input.txt")
        intcodeComputer.calculateIntcode(memory.toMutableList(), input)
        println(intcodeComputer.output)
    }
}

fun main() {
    ThermalEnvironmentSupervisionTerminal().runTEST(1)
    ThermalEnvironmentSupervisionTerminal().runTEST(5)
}
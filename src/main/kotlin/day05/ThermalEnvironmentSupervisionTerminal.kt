package day05

import Intcode.IntcodeComputer

class ThermalEnvironmentSupervisionTerminal {

    fun runTEST() {
        val intcodeComputer = IntcodeComputer()
        val memory = intcodeComputer.readInput("src/main/resources/day05/input.txt")
        intcodeComputer.calculateIntcode(memory.toMutableList(), 1)
    }
}

fun main() {
    ThermalEnvironmentSupervisionTerminal().runTEST()
}
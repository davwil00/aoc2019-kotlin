package day07

import intcode.IntcodeComputer

class AmplifierController {

    fun findMaxSettings(program: List<Int>, useFeedbackLoop: Boolean): Pair<Int, List<Int>> {
        var max = 0
        var maxSettings = listOf<Int>()
        val allowedValues = if (useFeedbackLoop) setOf(5, 6, 7, 8, 9) else setOf(0, 1, 2, 3, 4)
        generateCombinations(allowedValues)
            .map { it.toList() }
            .forEach {
                val output = if (useFeedbackLoop) calculateOutputSignalWithFeedbackLoop(program, it)
                else calculateOutputSignal(program, it)
                if (output > max) {
                    max = output
                    maxSettings = it
                }
            }

        return Pair(max, maxSettings)
    }

    private fun calculateOutputSignal(program: List<Int>, inputs: List<Int>): Int {
        var input2 = 0
        inputs.forEach { it ->
            val intcodeComputer = IntcodeComputer(program.toMutableList(), mutableListOf(it, input2))
            val output = intcodeComputer.calculateIntcode()
            input2 = output.last()
        }

        return input2
    }

    class Amp(val amp: IntcodeComputer, val name: Char) {
        lateinit var nextAmp: Amp
    }

    private fun calculateOutputSignalWithFeedbackLoop(program: List<Int>, inputs: List<Int>): Int {
        val ampA = Amp(IntcodeComputer(program.toMutableList(), mutableListOf(inputs[0], 0)), 'A')
        val ampB = Amp(IntcodeComputer(program.toMutableList(), mutableListOf(inputs[1])), 'B')
        val ampC = Amp(IntcodeComputer(program.toMutableList(), mutableListOf(inputs[2])), 'C')
        val ampD = Amp(IntcodeComputer(program.toMutableList(), mutableListOf(inputs[3])), 'D')
        val ampE = Amp(IntcodeComputer(program.toMutableList(), mutableListOf(inputs[4])), 'E')
        ampA.nextAmp = ampB
        ampB.nextAmp = ampC
        ampC.nextAmp = ampD
        ampD.nextAmp = ampE
        ampE.nextAmp = ampA

        runAmp(ampA)
        return ampE.amp.getOutput()
    }

    private tailrec fun runAmp(amp: Amp) {
        try {
            amp.amp.calculateIntcode()
        } catch (e: IndexOutOfBoundsException) {
        }

        amp.nextAmp.amp.supplyInput(amp.amp.getOutput())
        if (!amp.nextAmp.amp.ended) runAmp(amp.nextAmp)
    }

    private fun generateCombinations(allowedValues: Set<Int>): List<Set<Int>> {
        val min = allowedValues.sorted().joinToString("").toInt()
        val max = allowedValues.sortedDescending().joinToString("").toInt()
        val regex = Regex("^(?:([${allowedValues.min()}-${allowedValues.max()}])(?!.*\\1)){5}\$")
        val permutations = mutableListOf<Set<Int>>()
        (min..max)
            .map { it.toString().padStart(5, '0') }
            .filter { it.matches(regex) && it.length == 5 }
            .map { it.map { c -> c.toString().toInt() } }
            .forEach { permutations.add(it.toSet()) }

        return permutations
    }
}

fun main() {
    val program = IntcodeComputer.readInput("src/main/resources/day07/input.txt")
    println(AmplifierController().findMaxSettings(program, false))
    println(AmplifierController().findMaxSettings(program, true))
}
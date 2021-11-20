package day07

import intcode.IntcodeComputer

class AmplifierController {

    fun findMaxSettings(program: List<Long>, useFeedbackLoop: Boolean): Pair<Long, List<Long>> {
        var max = 0L
        var maxSettings = listOf<Long>()
        val allowedValues = if (useFeedbackLoop) setOf(5L, 6L, 7L, 8L, 9L) else setOf(0L, 1L, 2L, 3L, 4L)
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

    private fun calculateOutputSignal(program: List<Long>, inputs: List<Long>): Long {
        var input2 = 0L
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

    private fun calculateOutputSignalWithFeedbackLoop(program: List<Long>, inputs: List<Long>): Long {
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

    private fun generateCombinations(allowedValues: Set<Long>): List<Set<Long>> {
        val min = allowedValues.sorted().joinToString("").toLong()
        val max = allowedValues.sortedDescending().joinToString("").toLong()
        val regex = Regex("^(?:([${allowedValues.minOrNull()}-${allowedValues.maxOrNull()}])(?!.*\\1)){5}\$")
        val permutations = mutableListOf<Set<Long>>()
        (min..max)
            .map { it.toString().padStart(5, '0') }
            .filter { it.matches(regex) && it.length == 5 }
            .map { it.map { c -> c.toString().toLong() } }
            .forEach { permutations.add(it.toSet()) }

        return permutations
    }
}

fun main() {
    val program = IntcodeComputer.readInput("src/main/resources/day07/input.txt")
    println(AmplifierController().findMaxSettings(program, false))
    println(AmplifierController().findMaxSettings(program, true))
}
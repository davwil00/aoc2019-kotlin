package day09

import intcode.IntcodeComputer
import jdk.nashorn.internal.ir.annotations.Ignore
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BasicOperationOfSystemTestTest {

    @Test
    fun quineTest() {
        val program: MutableList<Long> = mutableListOf(109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99)
        val intcodeComputer = IntcodeComputer(program, mutableListOf())
        val result = intcodeComputer.calculateIntcode()

        assertEquals(listOf<Long>(109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99), result)
    }

    @Test
    fun longNumberTest() {
        val program: MutableList<Long> = mutableListOf(1102,34915192,34915192,7,4,7,99,0)
        val intcodeComputer = IntcodeComputer(program, mutableListOf())
        intcodeComputer.calculateIntcode()

        assertEquals(16, intcodeComputer.getOutput().toString().length)
    }

    @Test
    fun longNumberTest2() {
        val program = mutableListOf(104,1125899906842624,99)
        val result = IntcodeComputer(program, mutableListOf()).calculateIntcode()

        assertEquals(listOf(1125899906842624), result)
    }
}
package day09

import intcode.IntcodeComputer
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
        val program = mutableListOf(104,1125899906842624L,99)
        val result = IntcodeComputer(program, mutableListOf()).calculateIntcode()

        assertEquals(listOf(1125899906842624), result)
    }

    @Test
    fun testInputInMode0() {
        val program = listOf<Long>(3,10,4,10,3,11,4,11,99)
        val intcodeComputer = IntcodeComputer(program, mutableListOf(1, 2))
        val result = intcodeComputer.calculateIntcode()

        assertEquals(listOf<Long>(1, 2), result)
    }

    @Test
    fun testInputInMode2() {
        val program = listOf<Long>(
            109,20, // set offset to 20
            203,10, // read input and store in position 30
            4,30, // output value at position 30
            3,11, // read input and store in pos 31
            4,11, // output value in pos 31
            99, 31)
        val intcodeComputer = IntcodeComputer(program, mutableListOf(1, 2))
        val result = intcodeComputer.calculateIntcode()

        assertEquals(listOf<Long>(1, 2), result)
    }
}
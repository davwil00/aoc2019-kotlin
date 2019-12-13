package Intcode

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class IntcodeComputerTest {

    val testSubject = IntcodeComputer()

    @Test
    fun `calculateOpcode1 in position 0`() {
        val input = mutableListOf(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50)
        testSubject.opcode1(input, 0, listOf())

        assertEquals(listOf(1, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50), input)
    }

    @Test
    fun `calculateOpcode2 in position 0`() {
        val input = mutableListOf(1, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50)
        testSubject.opcode2(input, 4, listOf())

        assertEquals(listOf(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50), input)
    }

    @Test
    fun `calculateOpcode2 in position 1`() {
        val input = mutableListOf(1002, 4, 3, 4, 33)
        testSubject.opcode2(input, 0, listOf(IntcodeComputer.ParameterMode.POSITION, IntcodeComputer.ParameterMode.IMMEDIATE, IntcodeComputer.ParameterMode.POSITION))

        assertEquals(listOf(1002, 4, 3, 4, 99), input)
    }

    @ParameterizedTest
    @CsvSource("'1,9,10,3,2,3,11,0,99,30,40,50', '3500,9,10,70,2,3,11,0,99,30,40,50'",
        "'1,0,0,0,99', '2,0,0,0,99'",
        "'2,3,0,3,99', '2,3,0,6,99'",
        "'2,4,4,5,99,0', '2,4,4,5,99,9801'",
        "'1,1,1,4,99,5,6,0,99', '30,1,1,4,2,5,6,0,99'",
        "'1002,4,3,4,33', '1002,4,3,4,99'")
    fun calculateOptCode(inputStr: String, expectedStr: String) {
        val input = inputStr.split(",").map { it.toInt() }.toMutableList()
        val expected = expectedStr.split(",").map { it.toInt() }

        val actual = testSubject.calculateIntcode(input)

        assertEquals(expected, actual)
    }
}
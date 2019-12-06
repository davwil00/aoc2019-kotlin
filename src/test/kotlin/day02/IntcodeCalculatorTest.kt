package day02

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class IntcodeCalculatorTest {

    private val testSubject = IntcodeCalculator()

    @Test
    fun calculateOpcode1() {
        val input = mutableListOf(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50)
        testSubject.opcode1(input, 0)

        assertEquals(listOf(1, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50), input)
    }

    @Test
    fun calculateOpcode2() {
        val input = mutableListOf(1, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50)
        testSubject.opcode2(input, 4)

        assertEquals(listOf(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50), input)
    }

    @ParameterizedTest
    @CsvSource("'1,9,10,3,2,3,11,0,99,30,40,50', '3500,9,10,70,2,3,11,0,99,30,40,50'",
        "'1,0,0,0,99', '2,0,0,0,99'",
        "'2,3,0,3,99', '2,3,0,6,99'",
        "'2,4,4,5,99,0', '2,4,4,5,99,9801'",
        "'1,1,1,4,99,5,6,0,99', '30,1,1,4,2,5,6,0,99'")
    fun calculateOptCode(inputStr: String, expectedStr: String) {
        val input = inputStr.split(",").map { it.toInt() }.toMutableList()
        val expected = expectedStr.split(",").map { it.toInt() }

        val actual = testSubject.calculateIntcode(input)

        assertEquals(expected, actual)
    }
}
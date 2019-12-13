package intcode

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class IntcodeComputerTest {

    val testSubject = IntcodeComputer()

    @Test
    fun `calculateOpcode1 in position 0`() {
        val input = mutableListOf(1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50)
        testSubject.add(input, 0, listOf())

        assertEquals(listOf(1, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50), input)
    }

    @Test
    fun `calculateOpcode2 in position 0`() {
        val input = mutableListOf(1, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50)
        testSubject.multiply(input, 4, listOf())

        assertEquals(listOf(3500, 9, 10, 70, 2, 3, 11, 0, 99, 30, 40, 50), input)
    }

    @Test
    fun `calculateOpcode2 in position 1`() {
        val input = mutableListOf(1002, 4, 3, 4, 33)
        testSubject.multiply(input, 0, listOf(IntcodeComputer.ParameterMode.POSITION, IntcodeComputer.ParameterMode.IMMEDIATE, IntcodeComputer.ParameterMode.POSITION))

        assertEquals(listOf(1002, 4, 3, 4, 99), input)
    }

    @ParameterizedTest
    @CsvSource("'1,9,10,3,2,3,11,0,99,30,40,50', '3500,9,10,70,2,3,11,0,99,30,40,50'",
        "'1,0,0,0,99', '2,0,0,0,99'",
        "'2,3,0,3,99', '2,3,0,6,99'",
        "'2,4,4,5,99,0', '2,4,4,5,99,9801'",
        "'1,1,1,4,99,5,6,0,99', '30,1,1,4,2,5,6,0,99'",
        "'1002,4,3,4,33', '1002,4,3,4,99'")
    fun `calculateIntcode results in the correct memory state`(inputStr: String, expectedStr: String) {
        val input = inputStr.split(",").map { it.toInt() }.toMutableList()
        val expected = expectedStr.split(",").map { it.toInt() }

        val actual = testSubject.calculateIntcode(input)

        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @CsvSource("'3,9,8,9,10,9,4,9,99,-1,8', 7, 0",
        "'3,9,8,9,10,9,4,9,99,-1,8', 8, 1",
        "'3,9,8,9,10,9,4,9,99,-1,8', 10, 0",
        "'3,3,1108,-1,8,3,4,3,99', 7, 0",
        "'3,3,1108,-1,8,3,4,3,99', 8, 1",
        "'3,3,1108,-1,8,3,4,3,99', 10, 0")
    fun `calculateIntcode produces the correct output for optcode 8`(inputStr: String, input: Int, expectedOutput: Int) {
        val memory = inputStr.split(",").map { it.toInt() }.toMutableList()

        testSubject.calculateIntcode(memory, input)

        assertEquals(listOf(expectedOutput), testSubject.output)
    }

    @ParameterizedTest
    @CsvSource("'3,9,7,9,10,9,4,9,99,-1,8', 6, 1",
        "'3,9,7,9,10,9,4,9,99,-1,8', 8, 0",
        "'3,9,7,9,10,9,4,9,99,-1,8', 10, 0",
        "'3,3,1107,-1,8,3,4,3,99', 6, 1",
        "'3,3,1107,-1,8,3,4,3,99', 8, 0",
        "'3,3,1107,-1,8,3,4,3,99', 10, 0")
    fun `calculateIntcode produces the correct output for optcode 7`(inputStr: String, input: Int, expectedOutput: Int) {
        val memory = inputStr.split(",").map { it.toInt() }.toMutableList()

        testSubject.calculateIntcode(memory, input)

        assertEquals(listOf(expectedOutput), testSubject.output)
    }

    @ParameterizedTest
    @CsvSource("'3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9', 0, 0",
        "'3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9', 10, 1",
        "'3,3,1105,-1,9,1101,0,0,12,4,12,99,1', 0, 0",
        "'3,3,1105,-1,9,1101,0,0,12,4,12,99,1', -5, 1")
    fun `test jump-if-true and jump-if-false`(inputStr: String, input: Int, expectedOutput: Int) {
        val memory = inputStr.split(",").map { it.toInt() }.toMutableList()

        testSubject.calculateIntcode(memory, input)

        assertEquals(listOf(expectedOutput), testSubject.output)
    }

    @ParameterizedTest
    @CsvSource("1, 999",
        "8, 1000",
        "9, 1001")
    fun `test opcodes 1 to 8 are working`(input: Int, output: Int) {
        val memory = mutableListOf(3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
        1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
        999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99)

        testSubject.calculateIntcode(memory, input)
        assertEquals(listOf(output), testSubject.output)
    }
}
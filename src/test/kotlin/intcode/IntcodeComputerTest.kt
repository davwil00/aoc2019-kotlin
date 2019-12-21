package intcode

import intcode.IntcodeComputer.ParameterMode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class IntcodeComputerTest {

    @Test
    fun `calculateOpcode1 in position 0`() {
        val program: MutableList<Long> = mutableListOf(1, 5, 6, 3, 99, 30, 40, 50)
        val intcodeComputer = IntcodeComputer(program)
        intcodeComputer.calculateIntcode()

        assertEquals(listOf<Long>(1, 5, 6, 70, 99, 30, 40, 50), intcodeComputer.state)
    }

    @Test
    fun `calculateOpcode2 in position 0`() {
        val program: MutableList<Long> = mutableListOf(2, 5, 7, 0, 99, 70, 40, 50)
        val intcodeComputer = IntcodeComputer(program)
        intcodeComputer.calculateIntcode()

        assertEquals(listOf<Long>(3500, 5, 7, 0, 99, 70, 40, 50), intcodeComputer.state)
    }

    @Test
    fun `calculateOpcode2 in position 1`() {
        val program: MutableList<Long> = mutableListOf(1002, 4, 3, 4, 33)
        val intcodeComputer = IntcodeComputer(program)
        intcodeComputer.calculateIntcode()

        assertEquals(listOf<Long>(1002, 4, 3, 4, 99), intcodeComputer.state)
    }

    @ParameterizedTest
    @CsvSource("'1,9,10,3,2,3,11,0,99,30,40,50', '3500,9,10,70,2,3,11,0,99,30,40,50'",
        "'1,0,0,0,99', '2,0,0,0,99'",
        "'2,3,0,3,99', '2,3,0,6,99'",
        "'2,4,4,5,99,0', '2,4,4,5,99,9801'",
        "'1,1,1,4,99,5,6,0,99', '30,1,1,4,2,5,6,0,99'",
        "'1002,4,3,4,33', '1002,4,3,4,99'")
    fun `calculateIntcode results in the correct memory state`(inputStr: String, expectedStr: String) {
        val program = inputStr.split(",").map { it.toLong() }.toMutableList()
        val expected = expectedStr.split(",").map { it.toLong() }
        val intcodeComputer = IntcodeComputer(program)

        intcodeComputer.calculateIntcode()

        assertEquals(expected, intcodeComputer.state)
    }

    @ParameterizedTest
    @CsvSource("'3,9,8,9,10,9,4,9,99,-1,8', 7, 0",
        "'3,9,8,9,10,9,4,9,99,-1,8', 8, 1",
        "'3,9,8,9,10,9,4,9,99,-1,8', 10, 0",
        "'3,3,1108,-1,8,3,4,3,99', 7, 0",
        "'3,3,1108,-1,8,3,4,3,99', 8, 1",
        "'3,3,1108,-1,8,3,4,3,99', 10, 0")
    fun `calculateIntcode produces the correct output for optcode 8`(inputStr: String, input: Long, expectedOutput: Long) {
        val program = inputStr.split(",").map { it.toLong() }.toMutableList()

        val output = IntcodeComputer(program, mutableListOf(input)).calculateIntcode()

        assertEquals(listOf(expectedOutput), output)
    }

    @ParameterizedTest
    @CsvSource("'3,9,7,9,10,9,4,9,99,-1,8', 6, 1",
        "'3,9,7,9,10,9,4,9,99,-1,8', 8, 0",
        "'3,9,7,9,10,9,4,9,99,-1,8', 10, 0",
        "'3,3,1107,-1,8,3,4,3,99', 6, 1",
        "'3,3,1107,-1,8,3,4,3,99', 8, 0",
        "'3,3,1107,-1,8,3,4,3,99', 10, 0")
    fun `calculateIntcode produces the correct output for optcode 7`(inputStr: String, input: Long, expectedOutput: Long) {
        val program = inputStr.split(",").map { it.toLong() }.toMutableList()

        val output = IntcodeComputer(program, mutableListOf(input)).calculateIntcode()

        assertEquals(listOf(expectedOutput), output)
    }

    @ParameterizedTest
    @CsvSource("'3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9', 0, 0",
        "'3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9', 10, 1",
        "'3,3,1105,-1,9,1101,0,0,12,4,12,99,1', 0, 0",
        "'3,3,1105,-1,9,1101,0,0,12,4,12,99,1', -5, 1")
    fun `test jump-if-true and jump-if-false`(inputStr: String, input: Long, expectedOutput: Long) {
        val program = inputStr.split(",").map { it.toLong() }.toMutableList()

        val output = IntcodeComputer(program, mutableListOf(input)).calculateIntcode()

        assertEquals(listOf(expectedOutput), output)
    }

    @ParameterizedTest
    @CsvSource("1, 999",
        "8, 1000",
        "9, 1001")
    fun `test opcodes 1 to 8 are working`(input: Long, expected: Long) {
        val program: MutableList<Long> = mutableListOf(3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
        1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
        999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99)

        val actual = IntcodeComputer(program, mutableListOf(input)).calculateIntcode()
        assertEquals(listOf(expected), actual)
    }

    @Test
    fun testBoostOperations() {
        val program: MutableList<Long> = mutableListOf(109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99)

        val output = IntcodeComputer(program, mutableListOf()).calculateIntcode()
        assertEquals(listOf<Long>(109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99), output)
    }

    @Test
    fun readInstruction() {
        val intcodeComputer = IntcodeComputer(emptyList(), mutableListOf())

        var result = intcodeComputer.readInstruction(21108)
        assertEquals(Pair(listOf(ParameterMode.IMMEDIATE, ParameterMode.IMMEDIATE, ParameterMode.RELATIVE), 8L), result)

        result = intcodeComputer.readInstruction(8)
        assertEquals(Pair(listOf(ParameterMode.POSITION), 8L), result)

        result = intcodeComputer.readInstruction(1008)
        assertEquals(Pair(listOf(ParameterMode.POSITION, ParameterMode.IMMEDIATE), 8L), result)
    }

    @Test
    fun `test op1 in relative mode`() {
        val prog = listOf(109, 8, 1201, -8, 0, 63)
    }
}
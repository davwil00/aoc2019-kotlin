package day03

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class CrossedWiresLocatorTest {

    private val testSubject = CrossedWiresLocator()

    @ParameterizedTest
    @CsvSource("'R8,U5,L5,D3', 'U7,R6,D4,L4', 6",
        "'R75,D30,R83,U83,L12,D49,R71,U7,L72', 'U62,R66,U55,R34,D71,R55,D58,R83', 159", // wire crosses with itself?
        "'R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51', 'U98,R91,D20,R16,D67,R40,U7,R15,U6,R7', 135")
    fun `calculateCrossedWiresDistance should return the coordinates of the closest cross to the origin`(wire1Path: String, wire2Path: String, expectedDistance: Int) {
        val actual = testSubject.getCrossedWiresDistance(wire1Path, wire2Path)

        assertEquals(expectedDistance, actual)
    }
}
package day03

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class CrossedWiresLocatorTest {

    private val testSubject = CrossedWiresLocator()

    @ParameterizedTest
    @CsvSource("'R8,U5,L5,D3', 'U7,R6,D4,L4', 6",
        "'R75,D30,R83,U83,L12,D49,R71,U7,L72', 'U62,R66,U55,R34,D71,R55,D58,R83', 159",
        "'R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51', 'U98,R91,D20,R16,D67,R40,U7,R15,U6,R7', 135")
    fun `getCrossedWiresDistance should return the distance of the closest intersection to the origin`(wirePath1: String, wirePath2: String, expectedDistance: Int) {
        val actual = testSubject.getCrossedWiresDistance(wirePath1, wirePath2)

        assertEquals(expectedDistance, actual)
    }

    @ParameterizedTest
    @CsvSource("'R8,U5,L5,D3', 'U7,R6,D4,L4', 30",
        "'R75,D30,R83,U83,L12,D49,R71,U7,L72', 'U62,R66,U55,R34,D71,R55,D58,R83', 610",
        "'R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51', 'U98,R91,D20,R16,D67,R40,U7,R15,U6,R7', 410")
    fun `findMinStepsIntersection should find the intersection with the smallest number of steps`(wirePath1: String, wirePath2: String, expected: Int) {
        val actual = testSubject.getMinimumStepsIntersection(wirePath1, wirePath2)

        assertEquals(expected, actual)
    }
}
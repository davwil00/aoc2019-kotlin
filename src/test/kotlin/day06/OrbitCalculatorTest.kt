package day06

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class OrbitCalculatorTest {

    @Test
    fun calculateOrbits() {
        val input = """COM)B
           |B)C
           |C)D
           |D)E
           |E)F
           |B)G
           |G)H
           |D)I
           |E)J
           |J)K
           |K)L""".trimMargin()
        val totalOrbits = OrbitCalculator(input).calculateOrbits()
        assertEquals(42, totalOrbits)
    }

    @ParameterizedTest
    @CsvSource("'B,COM', B",
        "'D,C,B,COM', D")
    fun getPathToCom(expectedMap: String, obj: String) {
        val input = """COM)B
           |B)C
           |C)D
           |D)E
           |E)F
           |B)G
           |G)H
           |D)I
           |E)J
           |J)K
           |K)L""".trimMargin()
        val map = OrbitCalculator(input).getPathToCom(obj)
        assertEquals(expectedMap.split(','), map)
    }

    @Test
    fun `find path between orbits`() {
        val input = """COM)B
            |B)C
            |C)D
            |D)E
            |E)F
            |B)G
            |G)H
            |D)I
            |E)J
            |J)K
            |K)L
            |K)YOU
            |I)SAN""".trimMargin()
        val path = OrbitCalculator(input).findPathBetweenOrbits("YOU", "SAN")

        assertEquals(4, path)
    }
}
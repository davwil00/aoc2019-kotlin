package day01

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class FuelRequirementCalculatorTest {

    private val testSubject: FuelRequirementCalculator = FuelRequirementCalculator()

    @ParameterizedTest
    @CsvSource(
        "12, 2",
        "14, 2",
        "1969, 654",
        "100756, 33583"
    )
    fun `calculate fuel requirements for module`(mass: Int, expectedFuel: Int) {
        assertEquals(expectedFuel, testSubject.calculateFuel(mass))
    }

    @ParameterizedTest
    @CsvSource(
        "14, 2",
        "1969, 966",
        "100756, 50346"
    )
    fun `calculate fuel requirements for module and fuel`(mass: Int, expectedFuel: Int) {
        assertEquals(expectedFuel, testSubject.calculateFuelForModuleAndFuel(mass))
    }
}
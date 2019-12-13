package day04

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class PasswordDecrypterTest {

    private val testSubject = PasswordDecrypter()

    @ParameterizedTest
    @CsvSource("12, false",
        "99999, false",
        "123456, true",
        "1000000, false",
        "000000, true")
    fun `hasSixDigits should indicate whether the input has six digits`(input: String, expected: Boolean) {
        assertEquals(expected, testSubject.hasSixDigits(input))
    }

    @ParameterizedTest
    @CsvSource("12345, true",
        "11111, true",
        "12344, true",
        "12354, false",
        "55554, false")
    fun `digitsDoNotDecrease should indicate whether the input is in descending order`(input: String, expected: Boolean) {
        assertEquals(expected, testSubject.digitsDoNotDecrease(input))
    }

    @ParameterizedTest
    @CsvSource("112345, true",
        "111111, true",
        "123444, true",
        "123456, false",
        "119988, true")
    fun `hasTwoAdjacentDigits should indicate whether the input has at lease one pair of matching digits`(input: String, expected: Boolean) {
        assertEquals(expected, testSubject.hasTwoAdjacentDigits(input))
    }

    @ParameterizedTest
    @CsvSource("112233, true",
        "123444, false",
        "111122, true")
    fun `hasTwoAdjacentDigitsNotPartOfLargerGroup should indicate whether the input has at lease one pair of matching digits`(input: String, expected: Boolean) {
        assertEquals(expected, testSubject.hasTwoAdjacentDigitsNotPartOfLargerGroup(input))
    }

    @ParameterizedTest
    @CsvSource("555555, true",
        "223450, false",
        "123789, false")
    fun `isValidPassword should indicate whether potential password is valid`(passwordToTest: String, isValid: Boolean) {
        assertEquals(isValid, testSubject.isPasswordValid(passwordToTest))
    }
}
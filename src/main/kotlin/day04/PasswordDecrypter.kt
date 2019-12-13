package day04

class PasswordDecrypter {

    fun getNumberOfValidPasswords(): Int {
        return (MIN_VALUE..MAX_VALUE).count { isPasswordValid(it.toString()) }
    }

    fun getNumberOfValidPasswordsDay2(): Int {
        return (MIN_VALUE..MAX_VALUE).count { isPasswordValidDay2(it.toString()) }
    }

    fun isPasswordValid(passwordToTest: String): Boolean {
        return hasSixDigits(passwordToTest) &&
                hasTwoAdjacentDigits(passwordToTest) &&
                digitsDoNotDecrease(passwordToTest)
    }

    fun isPasswordValidDay2(passwordToTest: String): Boolean {
        return hasSixDigits(passwordToTest) &&
                hasTwoAdjacentDigitsNotPartOfLargerGroup(passwordToTest) &&
                digitsDoNotDecrease(passwordToTest)
    }

    fun hasSixDigits(passwordToTest: String) = passwordToTest.length == 6

    fun hasTwoAdjacentDigits(passwordToTest: String): Boolean {
        return passwordToTest.zipWithNext().any { (c1, c2) ->
            c1 == c2
        }
    }

    fun hasTwoAdjacentDigitsNotPartOfLargerGroup(passwordToTest: String): Boolean {
        val adjacentMatchingDigits = mutableListOf<String>()
        passwordToTest.zipWithNext().forEach { (c1, c2) ->
            if (c1 == c2) {
                adjacentMatchingDigits.add("$c1$c2")
            }
        }

        adjacentMatchingDigits.removeIf { pair ->
            adjacentMatchingDigits.count { it == pair } > 1
        }

        return adjacentMatchingDigits.size > 0
    }

    fun digitsDoNotDecrease(passwordToTest: String): Boolean {
        return passwordToTest.zipWithNext().all { (c1, c2) ->
            c1.toInt() <= c2.toInt()
        }
    }

    companion object {
        const val MIN_VALUE = 124075
        const val MAX_VALUE = 580769
    }
}

fun main() {
    val passwordDecrypter = PasswordDecrypter()
    println(passwordDecrypter.getNumberOfValidPasswords())
    println(passwordDecrypter.getNumberOfValidPasswordsDay2())
}
package day02

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class GravityAssistTest {

    private val testSubject = GravityAssist()

    @Test
    fun `completeGravityAssist should find noun and verb for input`() {
        val (noun, verb) = testSubject.completeGravityAssist(3516593)

        assertEquals(12L, noun)
        assertEquals(2L, verb)
    }
}
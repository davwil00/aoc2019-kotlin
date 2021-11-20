package day12

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class NBodyProblemTest {

    private val nBodyProblem = NBodyProblem()

    @Test
    fun `should update with correct gravity`() {
        val input = """<x=-1, y=0, z=2>
<x=2, y=-10, z=-7>
<x=4, y=-8, z=8>
<x=3, y=5, z=-1>"""
        val moons = nBodyProblem.parseInput(input)
        nBodyProblem.applyGravity(moons)
        moons.forEach { println(it) }
    }

    @Test
    fun `should calculate correct energy for 10 steps`() {
        val input = """<x=-1, y=0, z=2>
<x=2, y=-10, z=-7>
<x=4, y=-8, z=8>
<x=3, y=5, z=-1>"""
        val actual = nBodyProblem.simulateMotion(input, 10)
        assertThat(actual).isEqualTo(179)
    }

    @Test
    fun `should calculate correct energy for 100 steps`() {
        val input = """<x=-8, y=-10, z=0>
<x=5, y=5, z=10>
<x=2, y=-7, z=3>
<x=9, y=-8, z=-3>"""
        val actual = nBodyProblem.simulateMotion(input, 100)
        assertThat(actual).isEqualTo(1940)
    }

    @Test
    fun `should determine cycles`() {
        val input = """<x=-1, y=0, z=2>
<x=2, y=-10, z=-7>
<x=4, y=-8, z=8>
<x=3, y=5, z=-1>"""
        assertThat(nBodyProblem.determineCycle(nBodyProblem.parseInput(input))).isEqualTo(2772)
    }

    @Test
    fun `should determine longer cycles`() {
        val input = """<x=-8, y=-10, z=0>
<x=5, y=5, z=10>
<x=2, y=-7, z=3>
<x=9, y=-8, z=-3>"""
        assertThat(nBodyProblem.determineCycle(nBodyProblem.parseInput(input))).isEqualTo(4686774924)
    }
}
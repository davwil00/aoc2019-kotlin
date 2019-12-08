package day03

import java.io.File
import kotlin.math.abs

class CrossedWiresLocator {

    fun getCrossedWiresDistance(wirePath1: String, wirePath2: String): Int {
        val circuit = hashMapOf<Coordinate, Set<Int>>()
        plotWire(circuit, wirePath1, 1)
        plotWire(circuit, wirePath2, 2)

        val crossedWireLocations = circuit.filterValues { it.size > 1 }.keys
        return crossedWireLocations.map { abs(it.x) + abs(it.y) }.min()!!
    }

    private fun plotWire(circuit: Circuit, wirePath: String, pathNum: Int) {
        val currentLocation = Coordinate(0, 0)

        wirePath.split(",").forEach {
            val direction = it[0]
            val distance = it.substring(1).toInt()

            val operator = when (direction) {
                'U' -> Pair(1, 0)
                'D' -> Pair(-1, 0)
                'R' -> Pair(0, 1)
                'L' -> Pair(0, -1)
                else -> throw RuntimeException("Unexpected direction: $direction")
            }

            for (i in 0 until distance) {
                if (currentLocation.isNotOrigin()) circuit.merge(currentLocation.copy(), setOf(pathNum)) { x, y -> x + y }
                currentLocation += operator
            }
        }
    }

    data class Coordinate(var x: Int, var y: Int) {
        operator fun plusAssign (diff: Pair<Int, Int>) {
            x += diff.first
            y += diff.second
        }

        fun isNotOrigin() = !(x == 0 && y == 0)

        override fun toString(): String {
            return "($x, $y)"
        }
    }

}

typealias Circuit = MutableMap<CrossedWiresLocator.Coordinate, Set<Int>>

fun main() {
    val crossedWiresLocator = CrossedWiresLocator()
    val input = File("src/main/resources/day03/input.txt").readLines()
    print(crossedWiresLocator.getCrossedWiresDistance(input[0], input[1]))
}



package day03

import java.io.File
import kotlin.math.abs

class CrossedWiresLocator {

    fun getCrossedWiresDistance(wirePath1: String, wirePath2: String): Int {
        val circuit = hashMapOf<Coordinate, MutableSet<Point>>()
        plotWire(circuit, wirePath1, 1)
        plotWire(circuit, wirePath2, 2)

        val crossedWireLocations = circuit.filterValues { it.size > 1 }.keys
        return crossedWireLocations.map { abs(it.x) + abs(it.y) }.minOrNull()!!
    }

    fun getMinimumStepsIntersection(wirePath1: String, wirePath2: String): Int {
        val circuit = hashMapOf<Coordinate, MutableSet<Point>>()
        plotWire(circuit, wirePath1, 1)
        plotWire(circuit, wirePath2, 2)

        val crossedWireLocations = circuit.filterValues { it.size > 1 }.values
        return crossedWireLocations.map { it.fold(0) { acc: Int, p2: Point -> acc + p2.stepNo } }.minOrNull()!!
    }

    private fun plotWire(circuit: Circuit, wirePath: String, wireNo: Int) {
        val currentLocation = Coordinate(0, 0)
        var stepNo = 0

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
                if (currentLocation.isNotOrigin()) circuit.merge(currentLocation.copy(), mutableSetOf(Point(stepNo, wireNo))) { x, y -> x.addAll(y); x }
                currentLocation += operator
                stepNo++
            }
        }
    }

}

class Point(val stepNo: Int, val wireNo: Int) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point

        if (wireNo != other.wireNo) return false

        return true
    }

    override fun hashCode(): Int {
        return wireNo
    }

    override fun toString(): String {
        return "stepNo: $stepNo, wireNo: $wireNo"
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

typealias Circuit = MutableMap<Coordinate, MutableSet<Point>>

fun main() {
    val crossedWiresLocator = CrossedWiresLocator()
    val input = File("src/main/resources/day03/input.txt").readLines()
    println(crossedWiresLocator.getCrossedWiresDistance(input[0], input[1]))
    println(crossedWiresLocator.getMinimumStepsIntersection(input[0], input[1]))
}



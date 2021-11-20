package day11

import intcode.IntcodeComputer

class SpacePolice {

    fun paintItBlack(initialColour: Boolean = false): Map<Point, Boolean> {
        val program = IntcodeComputer.readInput("src/main/resources/day11/input.txt")
        val hull = mutableMapOf<Point, Boolean>().withDefault { false }
        val intcodeComputer = IntcodeComputer(program, mutableListOf())
        var pointer = 0
        var currentPosition = Position(Point(0, 0), 0)
        hull[currentPosition.position] = initialColour

        while(!intcodeComputer.ended) {
            val currentPanelColour: Long = if (hull.getValue(currentPosition.position)) 1 else 0
            try {
                intcodeComputer.supplyInput(currentPanelColour)
                intcodeComputer.calculateIntcode()
            } catch (e: IndexOutOfBoundsException) {
                val output = intcodeComputer.getAllOutput()
                val instructions = output.subList(pointer, pointer + 2)
                val colour = instructions[0]
                val turnDirection = instructions[1].toInt()
                hull[currentPosition.position] = (colour == 1L)
                currentPosition = move(currentPosition, turnDirection)
                pointer += 2
            }
        }

        return hull
    }

    private fun move(currentPosition: Position, turnDirection: Int): Position {
        val position= currentPosition.position
        val direction = currentPosition.direction

        var newDirection = when(turnDirection) {
            0 -> direction - 90
            1 -> direction + 90
            else -> throw IllegalArgumentException("Unexpected output: $turnDirection")
        }

        newDirection = when {
            newDirection < 0 -> newDirection + 360
            newDirection >= 360 -> newDirection - 360
            else -> newDirection
        }

        val newPosition = when(newDirection) {
            0 -> Point(position.first, position.second - 1)
            90 -> Point(position.first + 1, position.second)
            180 -> Point(position.first, position.second + 1)
            270 -> Point(position.first - 1, position.second)
            else -> throw IllegalArgumentException("Unexpected direction: $newDirection")
        }

        return Position(newPosition, newDirection)
    }

    fun printHull(hull: Map<Point, Boolean>) {
        val minX = hull.minByOrNull { (k, _) -> k.first }!!.key.first
        val maxX = hull.maxByOrNull { (k, _) -> k.first }!!.key.first
        val minY = hull.minByOrNull { (k, _) -> k.second }!!.key.second
        val maxY = hull.maxByOrNull { (k, _) -> k.second }!!.key.second

        for (y in minY..maxY) {
            for (x in minX..maxX) {
                print(if (hull.getValue(Point(x, y))) "\u2588" else " ")
            }
            println()
        }
    }

    private class Position(val position: Point, val direction: Int)
}

typealias Point = Pair<Int, Int>

fun main() {
    val spacePolice = SpacePolice()
    val hull = spacePolice.paintItBlack()
    println(hull.size)
    val newHull = spacePolice.paintItBlack(true)
    spacePolice.printHull(newHull)
}

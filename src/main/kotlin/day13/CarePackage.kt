package day13

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import intcode.IntcodeComputer
import intcode.IntcodeInputProvider
import org.slf4j.LoggerFactory

data class Instruction(val x: Int, val y: Int, val tileType: TileType)

enum class TileType(private val value: Int, val symbol: String) {
    EMPTY(0, " "),
    WALL(1, "\u2593"),
    BLOCK(2, "\u25A0"),
    HORIZONTAL_PADDLE(3, "_"),
    BALL(4, "o");

    companion object {
        fun fromValue(value: Long) =  values().find { it.value == value.toInt() }!!
    }
}

class BoardState(instructions: List<Long>) {
    var score = 0L
    var instructions = parseInput(instructions)

    private fun parseInput(input: List<Long>): List<Instruction> {
        return input.chunked(3).mapNotNull { values ->
            val (x, y, tileTypeOrScore) = values
            if (isScoreInstruction(x, y)) {
                score = tileTypeOrScore
                null
            } else {
                Instruction(x.toInt(), y.toInt(), TileType.fromValue(tileTypeOrScore))
            }
        }
    }

    private fun isScoreInstruction(x: Long, y: Long)  = x == -1L && y == 0L

    fun countBlockTiles() = instructions.filter { it.tileType == TileType.BLOCK }.size

    fun drawGame() {
        val maxX = instructions.maxOf { it.x } + 1
        val maxY = instructions.maxOf { it.y } + 1
        val screen = List(maxY) { MutableList(maxX) { TileType.EMPTY } }
        instructions.forEach { screen[it.y][it.x] = it.tileType }
        screen.forEach { row ->
            row.forEach { col -> print(col.symbol) }
            println()
        }
    }

    private fun getPositionOfTile(tileType: TileType) = instructions.findLast { it.tileType == tileType }!!

    fun calculateDirection(): Int {
        val ballPosition = getPositionOfTile(TileType.BALL)
        val paddlePosition = getPositionOfTile(TileType.HORIZONTAL_PADDLE)

        return when {
            ballPosition.x < paddlePosition.x -> -1
            ballPosition.x > paddlePosition.x -> 1
            else -> 0
        }
    }
}

class GameRunner: IntcodeInputProvider {
    var boardState: BoardState? = null

    override fun getNext(intcodeComputer: IntcodeComputer): Long {
        val boardState = BoardState(intcodeComputer.getAllOutput())
        return boardState.calculateDirection().toLong()
    }
}


fun main() {
    (LoggerFactory.getILoggerFactory() as LoggerContext).getLogger(IntcodeComputer::class.java).level = Level.INFO
    val input = IntcodeComputer.readInput("src/main/resources/day13/input.txt").toMutableList()
    println("There are ${BoardState(IntcodeComputer(input).calculateIntcode()).countBlockTiles()} block tiles")
    input[0] = 2
    val intcode = IntcodeComputer(input, mutableListOf())
    val gameRunner = GameRunner()
    intcode.registerInputProvider(gameRunner)
    val output = intcode.calculateIntcode()
    val finalState = BoardState(output)
    println("The final score is ${finalState.score}")
}

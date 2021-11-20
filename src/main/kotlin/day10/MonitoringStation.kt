package day10

import org.slf4j.LoggerFactory
import java.io.File
import java.lang.IllegalStateException
import kotlin.math.abs

class MonitoringStation {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun createPotentialLocationsMap(input: String): Map<Point, Int> {
        val map = createMapOfAsteroids(input)
        return calculateNumberOfAsteroidsVisibleAtEachPoint(map)
    }

    fun findMonitoringStationLocation(map: Map<Point, Int>) =
        map.maxByOrNull { (_, locations) -> locations }!!.key

    private fun createMapOfAsteroids(input: String): Map<Point, MutableList<AsteroidLocation>> {
        val map = mutableMapOf<Point, MutableList<AsteroidLocation>>()
        input.lines().forEachIndexed { y, line ->
            line.forEachIndexed { x, point ->
                if (point == '#') {
                    map[Pair(x, y)] = mutableListOf()
                }
            }
        }

        return map
    }

    private fun calculateNumberOfAsteroidsVisibleAtEachPoint(map: Map<Point, MutableList<AsteroidLocation>>): Map<Point, Int> {
        val visibleAsteroidsMap = mutableMapOf<Point, Int>()
        map.keys.forEach { point ->
            visibleAsteroidsMap[point] = getVisibleAsteroids(point, map.keys).size
        }

        return visibleAsteroidsMap
    }

    private fun getVisibleAsteroids(point: Point, asteroids: Collection<Point>): MutableList<AsteroidLocation> {
        val locations = mutableListOf<AsteroidLocation>()
        asteroids.filter { it != point }.forEach { other ->
            locations += AsteroidLocation(other, calculateM(point, other), calculateZone(point, other))
        }

        locations.retainAll(locations
            .sortedBy { manhattanDistance(point, it.point) }
            .distinctBy { asteroidLocation -> Pair(asteroidLocation.m, asteroidLocation.zone) })

        return locations
    }

    private fun calculateM(a: Point, b: Point): Double {
        val xDiff = b.first - a.first
        val yDiff = (b.second - a.second).toDouble()
        return if (xDiff == 0) Double.NEGATIVE_INFINITY else yDiff / xDiff
    }

    private fun calculateZone(a: Point, b: Point): Int {
        return when {
            b.second < a.second && b.first >= a.first -> 1
            b.second >= a.second && b.first > a.first -> 2
            b.second > a.second && b.first <= a.first -> 3
            b.second <= a.second && b.first < a.first -> 4
            else -> throw IllegalStateException("This can't happen")
        }
    }

    fun simulateLaserFire(stationLocation: Point, asteroids: Collection<Point>): List<Point> {
        val asteroidsDestroyed = mutableListOf<Point>()
        val asteroidsRemaining = asteroids.toMutableList()
        asteroidsRemaining.removeIf{ it == stationLocation }

        while (asteroidsRemaining.isNotEmpty()) {
            val visibleAsteroids = getVisibleAsteroids(stationLocation, asteroidsRemaining)
            asteroidsRemaining.removeAll(visibleAsteroids.map { it.point })

            while (visibleAsteroids.isNotEmpty()) {
                val asteroidDestroyed = getNextAsteroid(stationLocation, visibleAsteroids)
                logger.debug("Vaporizing asteroid at {}", asteroidDestroyed.point)
                asteroidsDestroyed += asteroidDestroyed.point
                visibleAsteroids -= asteroidDestroyed
            }
        }

        return asteroidsDestroyed
    }

    private fun getNextAsteroid(stationLocation: Point, asteroidsRemaining: List<AsteroidLocation>): AsteroidLocation {
        return asteroidsRemaining
            .sortedWith(compareBy(AsteroidLocation::zone, AsteroidLocation::m, { manhattanDistance(stationLocation, it.point) }))
            .first()
    }

    private fun manhattanDistance(a: Point, b: Point) = abs(a.first - b.first) + abs(a.second - b.second)
}

typealias Point = Pair<Int, Int>

class AsteroidLocation(val point: Point, val m: Double, val zone: Int) {
    override fun toString(): String {
        return "$point M=$m Z=$zone"
    }
}

fun main() {
    val input = File("src/main/resources/day10/input.txt").readText()
    val monitoringStation = MonitoringStation()
    val map = monitoringStation.createPotentialLocationsMap(input)
    val location = (monitoringStation.findMonitoringStationLocation(map))
    println(map[location])
    val asteroidsDestroyed = monitoringStation.simulateLaserFire(location, map.keys)
    println("200th asteroid destroyed = ${asteroidsDestroyed[199]}")
}
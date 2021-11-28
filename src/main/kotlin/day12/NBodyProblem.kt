package day12

import kotlin.math.abs

class NBodyProblem {

    fun simulateMotion(input: String, steps: Int): Long {
        val moons = parseInput(input)
        repeat(steps) {
            applyGravity(moons)
            moons.forEach(::println)
            println(moons.sumOf(Moon::calculateTotalEnergy))
        }
        
        return moons.sumOf(Moon::calculateTotalEnergy)
    }

    // Calculate periodicity of each plane for each moon
    fun determineCycle(moons: List<Moon>): Long {
        val periodicity = Periodicity()
        var i = 0L

        while (!periodicity.hasAll()) {
            applyGravity(moons)
            i++

            if (!periodicity.hasX() && foundPeriodicity(moons, Moon::isAtInitialPositionX)) {
                periodicity.x = i
            }

            if (!periodicity.hasY() && foundPeriodicity(moons, Moon::isAtInitialPositionY)) {
                periodicity.y = i
            }

            if (!periodicity.hasZ() && foundPeriodicity(moons, Moon::isAtInitialPositionZ)) {
                periodicity.z = i
            }
        }

        return periodicity.lcm()
    }

    internal fun parseInput(input: String): List<Moon> =
        input.lines()
             .mapNotNull(Moon.Companion::fromString)

    internal fun applyGravity(moons: List<Moon>) {
        generateCombinations(moons)
            .forEach { (moon1, moon2) -> applyGravity(moon1, moon2) }
        moons.forEach(Moon::applyVelocityToPosition)
    }

    private fun applyGravity(moon1: Moon, moon2: Moon) {
        val (velocityDeltaX1, velocityDeltaX2) = calculateVelocityDelta(moon1.position.x, moon2.position.x)
        val (velocityDeltaY1, velocityDeltaY2) = calculateVelocityDelta(moon1.position.y, moon2.position.y)
        val (velocityDeltaZ1, velocityDeltaZ2) = calculateVelocityDelta(moon1.position.z, moon2.position.z)

        moon1.applyGravityToVelocity(VelocityDelta(velocityDeltaX1, velocityDeltaY1, velocityDeltaZ1))
        moon2.applyGravityToVelocity(VelocityDelta(velocityDeltaX2, velocityDeltaY2, velocityDeltaZ2))
    }

    private fun calculateVelocityDelta(pos1: Long, pos2: Long): Pair<Long, Long> =
        when {
            (pos1 > pos2) -> Pair(-1, 1)
            (pos2 > pos1) -> Pair(1, -1)
            else -> Pair(0, 0)
        }

    private fun <T> generateCombinations(list: List<T>): List<Pair<T, T>> {
        return list.flatMapIndexed { i, first ->
            list.subList(i + 1, list.size).map { second -> Pair(first, second) }
        }
    }

    private fun foundPeriodicity(moons: List<Moon>, initialPositionCheckFun: (Moon) -> Boolean): Boolean {
        return moons.all(initialPositionCheckFun)
    }

    class Moon(private val initialPosition: Position) {
        var position = initialPosition.copy()
        var velocity = Velocity(0, 0, 0)

        fun applyGravityToVelocity(velocityDelta: VelocityDelta) {
            velocity += velocityDelta
        }

        fun applyVelocityToPosition() {
            position += velocity
        }

        fun isAtInitialPositionX() = position.x == initialPosition.x && velocity.x == 0L

        fun isAtInitialPositionY() = position.y == initialPosition.y && velocity.y == 0L

        fun isAtInitialPositionZ() = position.z == initialPosition.z && velocity.z == 0L

        private fun calculatePotentialEnergy() = position.absSum()

        private fun calculateKineticEnergy() = velocity.absSum()

        fun calculateTotalEnergy() = calculatePotentialEnergy() * calculateKineticEnergy()

        override fun toString(): String {
            return """pos=<$position>,vel=<$velocity>"""
        }

        companion object {
            private val inputRegex = Regex("""<x=([-\d]+), y=([-\d]+), z=([-\d]+)>""")

            fun fromString(input: String): Moon? =
                inputRegex.matchEntire(input)?.let { result ->
                    val (x, y, z) = result.groupValues.subList(1, 4).map(String::toLong)
                    Moon(Position(x, y, z))
                }
        }
    }
}

typealias VelocityDelta = XYZ
typealias Position = XYZ
typealias Velocity = XYZ

data class XYZ(var x: Long, var y: Long, var z: Long) {
    operator fun plus(other: VelocityDelta) = VelocityDelta(this.x + other.x, this.y + other.y, this.z + other.z)
    fun absSum() = abs(x) + abs(y) + abs(z)
    override fun toString() = "x=$x, y=$y, z=$z"
}

data class Periodicity(var x: Long? = null, var y: Long? = null, var z: Long? = null) {
    fun hasX() = x != null
    fun hasY() = y != null
    fun hasZ() = z != null
    fun hasAll() = hasX() && hasY() && hasZ()

    // @link https://en.wikipedia.org/wiki/Euclidean_algorithm#Implementations
    private tailrec fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

    // @link https://github.com/nickleefly/node-lcm/blob/5d44997/index.js
    private fun lcm(a: Long, b: Long) = if (b == 0L) 0 else (a * b) / gcd(a, b)

    fun lcm() = listOfNotNull(x, y, z).reduce { a, b -> lcm(a, b) }
}

fun main() {
    val input = """<x=5, y=-1, z=5>
<x=0, y=-14, z=2>
<x=16, y=4, z=0>
<x=18, y=1, z=16>"""
    val nBodyProblem = NBodyProblem()
    println("Part 1: ${nBodyProblem.simulateMotion(input, 1000)}")
    println("Part 2: ${nBodyProblem.determineCycle(nBodyProblem.parseInput(input))}")
}
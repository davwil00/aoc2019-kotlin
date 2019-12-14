package day06

import java.io.File

class OrbitCalculator(input: String) {

    private val orbitMap = constructOrbitMap(input)

    fun calculateOrbits(): Int {
        return orbitMap.keys.map {calculateIndirectOrbits(it)}.sum() - 1
    }

    fun findPathBetweenOrbits(obj1: String, obj2: String): Int {
        val path1 = getPathToCom(obj1)
        val path2 = getPathToCom(obj2)

        val union = path1.union(path2).toMutableList()
        union.removeAll(path1.intersect(path2))

        return union.size - 2
    }

    private fun constructOrbitMap(input: String): MutableMap<String, String> {
        val orbitMap = mutableMapOf(Pair("", "COM"))
        input.lines().forEach {
            val (lhs, rhs) = it.split(')')
            orbitMap[rhs] = lhs
        }
        return orbitMap
    }

    private tailrec fun calculateIndirectOrbits(obj: String?, orbits: Int = 0): Int {
        return if (obj == "COM") orbits else calculateIndirectOrbits(orbitMap[obj], orbits + 1)
    }

    tailrec fun getPathToCom(obj: String?, path: List<String?> = emptyList()): List<String?> {
        return if (obj == "COM") path.plus("COM") else getPathToCom(orbitMap[obj], path.plus(obj))
    }
}

fun main() {
    val input = File("src/main/resources/day06/input.txt").readText()
    val orbitCalculator = OrbitCalculator(input)
    println(orbitCalculator.calculateOrbits())
    println(orbitCalculator.findPathBetweenOrbits("YOU", "SAN"))
}
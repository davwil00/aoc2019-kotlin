package day01

import java.io.File
import kotlin.math.floor
import kotlin.math.max

class FuelRequirementCalculator {

    fun calculateTotalFuel(): Int = calculateTotal(::calculateFuel)

    fun calculateTotalFuelIncFuelWeight(): Int = calculateTotal{ mass -> calculateFuelForModuleAndFuel(mass) }

    fun calculateFuel(mass: Int): Int = max(0, floor(mass / 3.0).toInt() - 2)

    tailrec fun calculateFuelForModuleAndFuel(mass: Int, total: Int = 0): Int {
        val fuelForMass = calculateFuel(mass)
        return if (fuelForMass <= 0) total else calculateFuelForModuleAndFuel(fuelForMass, fuelForMass + total)
    }

    private fun calculateTotal(calculator: (mass: Int) -> Int): Int {
        var total = 0
        readInput().forEachLine {
            val mass = Integer.parseInt(it)
            total += calculator(mass)
        }

        return total
    }

    private fun readInput() = File("src/main/resources/day01/input.txt")
}

fun main() {
    val fuelRequirementCalculator = FuelRequirementCalculator()
    println(fuelRequirementCalculator.calculateTotalFuel())
    println(fuelRequirementCalculator.calculateTotalFuelIncFuelWeight())
}
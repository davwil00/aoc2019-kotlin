package day01

import java.io.File
import kotlin.math.floor
import kotlin.math.max

class FuelRequirementCalculator {

    fun calculateTotalFuel(): Int {
        var total = 0
        readInput().forEachLine {
            val mass = Integer.parseInt(it)
            total += calculateFuel(mass)
        }

        return total
    }

    fun calculateTotalFuelIncFuelWeight(): Int {
        var total = 0
        readInput().forEachLine {
            val mass = Integer.parseInt(it)
            total += calculateFuelForModuleAndFuel(mass)
        }

        return total
    }

    fun calculateFuel(mass: Int): Int = max(0, floor(mass / 3.0).toInt() - 2)

    fun calculateFuelForModuleAndFuel(mass: Int): Int {
        var totalFuel = calculateFuel(mass)
        var additionalFuelRequired = totalFuel

        do {
            additionalFuelRequired = calculateFuel(additionalFuelRequired)
            totalFuel += additionalFuelRequired
        } while (additionalFuelRequired > 0)

        return totalFuel
    }

    private fun readInput() = File("src/main/resources/day01/input.txt")
}

fun main() {
    val fuelRequirementCalculator = FuelRequirementCalculator()
    println(fuelRequirementCalculator.calculateTotalFuel())
    println(fuelRequirementCalculator.calculateTotalFuelIncFuelWeight())
}
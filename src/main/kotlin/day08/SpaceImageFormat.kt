package day08

import java.io.File

class SpaceImageFormat {

    fun splitLayers(input: List<Int>, width: Int, height: Int): List<Layer> {
        var x = 0
        var y = 0
        val layers = mutableListOf<Layer>()
        var layer = mutableListOf<List<Int>>()
        var row = mutableListOf<Int>()

        input.forEach {
            row.add(it)
            x++
            if (x == width) {
                x = 0
                layer.add(row)
                row = mutableListOf()
                y++
            }
            if (y == height) {
                y = 0
                layers.add(layer)
                row = mutableListOf()
                layer = mutableListOf()
            }
        }

        return layers
    }

    fun findLayerWithFewestZeros(layers: List<Layer>): Int {
        val zerosInLayers = layers.map { countOccurrencesOfValInLayer(it, 0) }
        return zerosInLayers.indexOf(zerosInLayers.minOrNull())
    }

    fun countOccurrencesOfValInLayer(layer: Layer, valToCount: Int): Int {
        return layer.sumBy { row -> row.count { it == valToCount } }
    }

    fun mergeLayers(layers: List<Layer>, width: Int, height: Int): List<List<Int>> {
        val mergedLayers = MutableList(height){ MutableList(width) { 2 } }

        layers.forEach { layer ->
            layer.forEachIndexed { rowIdx, row ->
                row.forEachIndexed { colIdx, pixel ->
                    if (pixel < 2 && mergedLayers[rowIdx][colIdx] == 2) {
                        mergedLayers[rowIdx][colIdx] = pixel
                    }
                }
            }
        }

        return mergedLayers
    }

    fun printImage(layer: Layer) {
        layer.forEach { row ->
            row.forEach { pixel -> print(if (pixel == 1) "\u2588" else " ") }
            println()
        }
    }
}

fun main() {
    val input = File("src/main/resources/day08/input.txt").readText().map { it.toString().toInt() }
    val spaceImageFormat = SpaceImageFormat()
    val layers = spaceImageFormat.splitLayers(input, 25, 6)
    val layerWithFewestZeros = spaceImageFormat.findLayerWithFewestZeros(layers)
    val numberOf1s = spaceImageFormat.countOccurrencesOfValInLayer(layers[layerWithFewestZeros], 1)
    val numberOf2s = spaceImageFormat.countOccurrencesOfValInLayer(layers[layerWithFewestZeros], 2)

    println(numberOf1s * numberOf2s)
    spaceImageFormat.printImage(spaceImageFormat.mergeLayers(layers, 25, 6))
}

typealias Layer = List<List<Int>>

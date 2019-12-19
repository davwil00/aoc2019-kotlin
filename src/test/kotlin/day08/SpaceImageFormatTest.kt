package day08

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class SpaceImageFormatTest {

    @Test
    fun `image is parsed into layers correctly`() {
        val layers = SpaceImageFormat().splitLayers("123456789012".map { it.toString().toInt() }.toList(), 3, 2)
        val expected = listOf(listOf(listOf(1,2,3), listOf(4,5,6)), listOf(listOf(7,8,9), listOf(0,1,2)))
        assertEquals(expected, layers)
    }

    @Test
    fun `layers are merged correctly`() {
        val spaceImageFormat = SpaceImageFormat()
        val layers = spaceImageFormat.splitLayers("0222112222120000".map { it.toString().toInt() }.toList(), 2, 2)
        val mergedLayers = spaceImageFormat.mergeLayers(layers, 2, 2)
        val expected = listOf(listOf(0, 1), listOf(1, 0))
        assertEquals(expected, mergedLayers)
    }
}
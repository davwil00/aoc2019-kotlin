package day10

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MonitoringStationTest {

    @Test
    fun `find best position for monitoring station with small map`() {
        val input = """
            .#..#
            .....
            #####
            ....#
            ...##
        """.trimIndent()
        val monitoringStation = MonitoringStation()
        val map = monitoringStation.createPotentialLocationsMap(input)
        val result = monitoringStation.findMonitoringStationLocation(map)
        assertEquals(Pair(3, 4), result)
    }

    @Test
    fun `find best position with 34 asteroids`() {
        val input = """
            ......#.#.
            #..#.#....
            ..#######.
            .#.#.###..
            .#..#.....
            ..#....#.#
            #..#....#.
            .##.#..###
            ##...#..#.
            .#....####
        """.trimIndent()
        val monitoringStation = MonitoringStation()
        val map = monitoringStation.createPotentialLocationsMap(input)
        val result = monitoringStation.findMonitoringStationLocation(map)
        assertEquals(Pair(5, 8), result)
    }

    @Test
    fun `find best position with 35 asteroids`() {
        val input = """
            #.#...#.#.
            .###....#.
            .#....#...
            ##.#.#.#.#
            ....#.#.#.
            .##..###.#
            ..#...##..
            ..##....##
            ......#...
            .####.###.
        """.trimIndent()
        val monitoringStation = MonitoringStation()
        val map = monitoringStation.createPotentialLocationsMap(input)
        val result = monitoringStation.findMonitoringStationLocation(map)
        assertEquals(Pair(1, 2), result)
    }

    @Test
    fun `find best position with 41 asteroids`() {
        val input = """
            .#..#..###
            ####.###.#
            ....###.#.
            ..###.##.#
            ##.##.#.#.
            ....###..#
            ..#.#..#.#
            #..#.#.###
            .##...##.#
            .....#.#..
        """.trimIndent()
        val monitoringStation = MonitoringStation()
        val map = monitoringStation.createPotentialLocationsMap(input)
        val result = monitoringStation.findMonitoringStationLocation(map)
        assertEquals(Pair(6, 3), result)
    }

    @Test
    fun `find best position with 210 asteroids`() {
        val input = """
            .#..##.###...#######
            ##.############..##.
            .#.######.########.#
            .###.#######.####.#.
            #####.##.#.##.###.##
            ..#####..#.#########
            ####################
            #.####....###.#.#.##
            ##.#################
            #####.##.###..####..
            ..######..##.#######
            ####.##.####...##..#
            .#####..#.######.###
            ##...#.##########...
            #.##########.#######
            .####.#.###.###.#.##
            ....##.##.###..#####
            .#.#.###########.###
            #.#.#.#####.####.###
            ###.##.####.##.#..##
        """.trimIndent()
        val monitoringStation = MonitoringStation()
        val map = monitoringStation.createPotentialLocationsMap(input)
        val result = monitoringStation.findMonitoringStationLocation(map)
        assertEquals(Pair(11, 13), result)
    }

    @Test
    fun `find potential locations`() {
        /*
        .7..7
        .....
        67775
        ....7
        ...87
         */
        val map = """.#..#
                     |.....
                     |#####
                     |....#
                     |...##""".trimMargin()
        val expected = mapOf(
            Pair(1, 0) to 7, Pair(4, 0) to 7,
            Pair(0, 2) to 6, Pair(1, 2) to 7, Pair(2, 2) to 7, Pair(3, 2) to 7, Pair(4, 2) to 5,
            Pair(4, 3) to 7,
            Pair(3, 4) to 8,
            Pair(4, 4) to 7
        )
        val actual = MonitoringStation().createPotentialLocationsMap(map)
        assertEquals(expected, actual)
    }

    @Test
    fun `test vaporizing asteroids`() {
        val input = """
            .#....#####...#..
            ##...##.#####..##
            ##...#...#.#####.
            ..#.....#...###..
            ..#.#.....#....##
        """.trimIndent()
        val monitoringStation = MonitoringStation()
        val asteroidLocations = monitoringStation.createPotentialLocationsMap(input).keys
        val expected = listOf(
            Pair(8,1), Pair(9, 0), Pair(9,1), Pair(10,0), Pair(9,2), Pair(11,1), Pair(12,1), Pair(11,2), Pair(15,1),
            Pair(12,2), Pair(13,2), Pair(14,2), Pair(15,2), Pair(12,3), Pair(16,4), Pair(15,4), Pair(10,4), Pair(4,4),
            Pair(2,4), Pair(2,3), Pair(0, 2), Pair(1,2), Pair(0,1), Pair(1,1), Pair(5,2), Pair(1,0), Pair(5,1),
            Pair(6,1), Pair(6,0), Pair(7,0), Pair(8,0), Pair(10,1), Pair(14,0), Pair(16,1), Pair(13,3), Pair(14,3)

        )
        val actual = monitoringStation.simulateLaserFire(Pair(8, 3), asteroidLocations)
        assertEquals(expected, actual)
    }

    @Test
    fun `test simulate fire on large map`() {
        val input = """
            .#..##.###...#######
            ##.############..##.
            .#.######.########.#
            .###.#######.####.#.
            #####.##.#.##.###.##
            ..#####..#.#########
            ####################
            #.####....###.#.#.##
            ##.#################
            #####.##.###..####..
            ..######..##.#######
            ####.##.####...##..#
            .#####..#.######.###
            ##...#.##########...
            #.##########.#######
            .####.#.###.###.#.##
            ....##.##.###..#####
            .#.#.###########.###
            #.#.#.#####.####.###
            ###.##.####.##.#..##
        """.trimIndent()
        val monitoringStation = MonitoringStation()
        val map = monitoringStation.createPotentialLocationsMap(input)
        val asteroidsDestroyed = monitoringStation.simulateLaserFire(Pair(11,13), map.keys)

        assertEquals(Pair(11,12), asteroidsDestroyed[0])
        assertEquals(Pair(12,1), asteroidsDestroyed[1])
        assertEquals(Pair(12,2), asteroidsDestroyed[2])
        assertEquals(Pair(12,8), asteroidsDestroyed[9])
        assertEquals(Pair(16,0), asteroidsDestroyed[19])
        assertEquals(Pair(16,9), asteroidsDestroyed[49])
        assertEquals(Pair(10,16), asteroidsDestroyed[99])
        assertEquals(Pair(9,6), asteroidsDestroyed[198])
        assertEquals(Pair(8,2), asteroidsDestroyed[199])
        assertEquals(Pair(10,9), asteroidsDestroyed[200])
        assertEquals(Pair(11,1), asteroidsDestroyed[298])
    }
}
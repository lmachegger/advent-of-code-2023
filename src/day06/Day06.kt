package day06

import println
import readInput

fun main() {
    fun part1(input: List<String>): Long {
        val races = parseInput(input)
        return races.map { it.numberOfWins }.reduce { acc, value -> acc * value }
    }

    fun part2(input: List<String>): Long {
        val race = parseInputPart2(input)
        return race.numberOfWins
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day06/Day06_test")
    check(part1(testInput) == 288L)
    check(part2(testInput) == 71503L)

    val input = readInput("day06/Day06")

    part1(input).println() // 128700
    part2(input).println()
}

fun parseInput(input: List<String>): List<Race> {
    val times = input[0].substringAfter("Time:")
        .split(" ")
        .filter { it.isNotEmpty() && it.isNotBlank() }
        .map { it.toLong() }
    val distances = input[1].substringAfter("Distance: ")
        .split(" ")
        .filter { it.isNotEmpty() && it.isNotBlank() }
        .map { it.toLong() }
    return times.mapIndexed { index, time ->
        Race(time, distances[index])
    }
}

fun parseInputPart2(input: List<String>): Race {
    return Race(
        time = input[0].substringAfter("Time:").replace(" ", "").toLong(),
        distance = input[1].substringAfter("Distance:").replace(" ", "").toLong()
    )
}

class Race(private val time: Long, private val distance: Long) {
    val numberOfWins: Long
        get() {
            val min = (1 until time).find { speed ->
                speed * (time - speed) > distance
            }
            val max = (time - 1 downTo 1).find { speed ->
                speed * (time - speed) > distance
            }

            return (max!! - min!!) + 1
        }
}

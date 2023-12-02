package day02

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            parseGame(line).let {
                if (it.isValid(cubeRules)) it.id else 0
            }
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line ->
            parseGame(line).powerOfSet()
        }
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("day02/Day02_test")
    check(part1(testInput) == 8)

    check(part2(testInput) == 2286)

    val input = readInput("day02/Day02")
    part1(input).println()
    part2(input).println()
}

val cubeRules = mapOf(
    "red" to 12,
    "green" to 13,
    "blue" to 14
)

data class Game(
    val id: Int,
    val cubeSets: List<CubeSet>,
) {
    fun isValid(rules: Map<String, Int>): Boolean {
        return cubeSets
            .flatMap { it.cubes }
            .all { it.number <= rules[it.color]!! }
    }

    fun powerOfSet(): Int {
        val maxCubesPerSet = mutableMapOf<String, Int>()
        cubeSets.forEach { cubeSet ->
            cubeSet.cubes.forEach { cube ->
                maxCubesPerSet[cube.color] = maxCubesPerSet.getOrDefault(cube.color, 0).coerceAtLeast(cube.number)
            }
        }
        return maxCubesPerSet.values.reduce { acc, value -> acc * value }
    }
}

data class CubeSet(val cubes: List<Cube>)
data class Cube(val color: String, val number: Int)

/**
 * input: "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"
 */
fun parseGame(input: String): Game {
    val id = input
        .substringAfter("Game ")
        .substringBefore(":")
        .toInt()

    val cubeSets = input
        .substringAfter(": ")
        .split(";")
        .map { parseCubeSet(it) }

    return Game(id = id, cubeSets = cubeSets)
}

/**
 * input: "3 blue, 4 red
 */
fun parseCubeSet(input: String): CubeSet {
    return CubeSet(cubes = input
        .split(",")
        .map { parseCube(it) }
    )
}

/**
 * input: "3 blue"
 */
fun parseCube(input: String): Cube {
    input
        .split(" ")
        .filter { it.isNotBlank() }
        .also {
            return Cube(
                color = it[1],
                number = it[0].toInt()
            )
        }
}

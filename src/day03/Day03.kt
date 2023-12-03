package day03

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val parser = Parser(input)
        val partNumbers = parser.parseNumbers().filter { it.isValidPartNumber(input) }
        return partNumbers.sumOf { it.number }
    }

    fun part2(input: List<String>): Int {
        val parser = Parser(input)
        val numbers = parser.parseNumbers()

        // find all numbers with that star as a neighbour. If count == 2, multiply them. Sum it up.
        return parser.parseStars().sumOf { star ->
            val numbersWithThisNeighbour = numbers.filter { it.hasStarNeighbour(star, input) }

            if (numbersWithThisNeighbour.size == 2)
                numbersWithThisNeighbour[0].number * numbersWithThisNeighbour[1].number
            else
                0
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day03/Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("day03/Day03")
    check(part1(input) == 537832)
    check(part2(input) == 81939900)

    part1(input).println()
    part2(input).println()
}

class Parser(private val input: List<String>) {
    // find all PartNumbers
    fun parseNumbers(): List<PartNumber> {
        val partNumbers = mutableListOf<PartNumber>()
        input.forEachIndexed { y, line ->
            val points = mutableListOf<Point>()
            line.forEachIndexed { x, char ->
                if (char.isDigit()) {
                    points.add(Point(x, y, char))
                }
                if ((!char.isDigit() || x == line.length - 1) && points.isNotEmpty()) {
                    partNumbers.add(PartNumber(points.toList()))
                    points.clear()
                }
            }
        }

        return partNumbers
    }

    // part 2 - find all stars
    fun parseStars(): List<Point> {
        val stars = mutableListOf<Point>()
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                if (char == '*') stars.add(Point(x, y, char))
            }
        }

        return stars
    }
}


data class PartNumber(
    val points: List<Point>,
) {
    val number: Int = points.map { it.value }.joinToString("").toInt()

    fun isValidPartNumber(input: List<String>): Boolean {
        return points.any { it.getNeighbours(input).isNotEmpty() }
    }

    // part 2
    fun hasStarNeighbour(starNeighbour: Point, input: List<String>): Boolean {
        return points.flatMap { it.getNeighbours(input) }
            .filter { it.value == '*' }
            .contains(starNeighbour)
    }
}

data class Point(
    val x: Int,
    val y: Int,
    val value: Char
) {
    /**
     * Returns all neighbours that are not out of bounds, not a digit and not a dot.
     */
    fun getNeighbours(input: List<String>): List<Point> {
        val neighbours = mutableListOf<Point>()
        val limitX = input[0].length
        val limitY = input.size

        for (i in -1..1) {
            for (j in -1..1) {
                if (x + i in 0..<limitX && y + j in 0..<limitY) {
                    // don't add self
                    if (i == 0 && j == 0) continue

                    val char = input[y + j][x + i]
                    if (char.isDigit() || char == '.') continue

                    neighbours.add(Point(x + i, y + j, char))
                }
            }
        }
        return neighbours
    }
}

package day04

import kotlin.math.pow
import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return parseCards(input).sumOf { it.score }
    }

    fun part2(input: List<String>): Int {
        val originalCards = parseCards(input).map { CardCopy(it, 1) }

        originalCards.forEachIndexed { index, cardCopy ->
            for (i in 1..cardCopy.copies) {
                for (id in index + 1..index + cardCopy.card.matches) {
                    originalCards[id].copies++
                }
            }
        }

        return originalCards.sumOf { it.copies }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day04/Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("day04/Day04")

    part1(input).println()
    part2(input).println()
}


data class CardCopy(val card: Card, var copies: Int)

data class Card(
    val winningNumbers: List<Int>,
    val numbers: List<Int>,
) {
    // part 1
    val score: Int
        get() {
            val winners = numbers.count { it in winningNumbers }
            return if (winners == 0) 0 else 2.0.pow(winners - 1).toInt()
        }

    // part 2
    val matches: Int
        get() = numbers.count { it in winningNumbers }

}

fun parseCards(input: List<String>): List<Card> {
    return input.map { line ->
        Card(
            winningNumbers = line.substringAfter(": ")
                .substringBefore(" |")
                .split(" ")
                .filter { it.isNotEmpty() }
                .map { it.toInt() },
            numbers = line.substringAfter("| ")
                .split(" ")
                .filter { it.isNotEmpty() }
                .map { it.toInt() }
        )
    }
}

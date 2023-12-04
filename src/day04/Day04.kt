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

        originalCards.forEach { cardCopy ->
            for (i in 1..cardCopy.copies) {
                for (id in cardCopy.card.id + 1..cardCopy.card.id + cardCopy.card.matches) {
                    originalCards.find { it.card.id == id }?.let {
                        it.copies++
                    }
                }
            }
        }

        return originalCards.sumOf { it.copies }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day04/Day04_test")
    check(part1(testInput) == 13)

    val input = readInput("day04/Day04")

    part1(input).println()
    part2(input).println()
}

fun parseCards(input: List<String>): List<Card> {
    return input.map { line ->
        Card(
            id = line.substringAfter("Card ").substringBefore(":").trim().toInt(),
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

data class CardCopy(val card: Card, var copies: Int)

data class Card(
    val id: Int,
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
        get() {
            return numbers.count { it in winningNumbers }
        }
}

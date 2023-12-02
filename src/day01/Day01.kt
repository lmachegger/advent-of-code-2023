package day01

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line -> firstAndLastDigit(line) }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { line -> firstAndLastDigit(replaceSpelledDigits(line)) }
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val testInput2 = readInput("Day01_test_2")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

/**
 * Returns the first and last digit of the input.
 * e.g. "abc123" will return 13
 */
fun firstAndLastDigit(input: String): Int {
    return (input.first { it.isDigit() }.toString() + input.last { it.isDigit() }.toString()).toInt()
}

/**
 * Loop over chars. If char is a digit, keep it.
 * If char is a letter, find the possible words from that index, and try matching it with our dictionary.
 */
fun replaceSpelledDigits(input: String): String {
    return input.mapIndexedNotNull { index, char ->
        if (char.isDigit()) char
        else findWordsStartingAt(input, index).firstNotNullOfOrNull { dict[it] }
    }.joinToString()
}


/**
 * Returns a list of words starting at the given index.
 * The words are of length 3 to 5.
 * If the index is out of bounds, an empty list is returned.
 * e.g. input = "abcdefg", startIndex = 2 will result in ["cde", "cdef", "cdefg"]
 */
fun findWordsStartingAt(input: String, startIndex: Int): List<String> {
    val minWordLength = 3
    val maxWordLength = 5

    return (minWordLength..maxWordLength).map {
        input.substring(startIndex, (startIndex + it).coerceAtMost(input.length))
    }
}

val dict = mapOf(
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9",
)

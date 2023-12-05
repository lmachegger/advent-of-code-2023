package day05

import java.lang.Exception
import println
import readInput

fun main() {
    fun part1(input: List<String>): Long {
        val (seeds, mappings) = parseInput(input)

        return seeds.minOf { seed -> mappings.fold(seed) { acc, mapping -> mapping.mapValue(acc) } }
    }

    /**
     * Reverse the mapping from part 1:
     * 1. find the lowest location (doesn't really matter, just start at 0 and go up)
     * 2. map your way backwards
     * 3. check if the result is a valid seed
     * 4. if not, use the next lowest location
     */
    fun part2(input: List<String>): Long {
        val (seeds, mappings) = parseInput(input)
        val listOfSeedRanges = mutableListOf<Range>()
        for (i in seeds.indices step 2) {
            listOfSeedRanges.add(Range(seeds[i], seeds[i + 1]))
        }

        val reversed = mappings.reversed()

        for (location in 0L..<Long.MAX_VALUE) {
            val seed = reversed.fold(location) { acc, mapping -> mapping.mapValueReversed(acc) }

            for (range in listOfSeedRanges) {
                if (range.isValueInRange(seed)) {
                    return location
                }
            }
        }

        throw Exception("no seed found")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day05/Day05_test")
    check(part1(testInput) == 35.toLong())
    check(part2(testInput) == 46.toLong())

    val input = readInput("day05/Day05")

    part1(input).println()
    part2(input).println()
}

fun parseInput(input: List<String>): Pair<List<Long>, List<Mapping>> {
    val seeds = input[0].substringAfter(": ").split(" ").map { it.toLong() }
    val mappings = mutableListOf<Mapping>()
    val sourceRanges = mutableListOf<Range>()
    val destRanges = mutableListOf<Range>()
    for (i in 2 until input.size) {
        val line = input[i]

        if (line.isNotEmpty() && line[0].isDigit()) {
            val (dest, source, length) = line.split(" ").map { it.toLong() }
            sourceRanges.add(Range(source, length))
            destRanges.add(Range(dest, length))
        }
        if (line.isEmpty() || i == input.size - 1) {
            mappings.add(Mapping(sourceRanges.toList(), destRanges.toList()))
            sourceRanges.clear()
            destRanges.clear()
        }
    }

    return Pair(seeds, mappings)
}

data class Mapping(val sourceRanges: List<Range>, val destinationRanges: List<Range>) {
    fun mapValue(value: Long): Long {
        val sourceRange = sourceRanges.firstOrNull { it.isValueInRange(value) } ?: return value
        val destinationRange = destinationRanges[sourceRanges.indexOf(sourceRange)]

        val offset = destinationRange.start - sourceRange.start
        return value + offset
    }

    // Part 2
    fun mapValueReversed(value: Long): Long {
        val destinationRange = destinationRanges.firstOrNull { it.isValueInRange(value) } ?: return value
        val sourceRange = sourceRanges[destinationRanges.indexOf(destinationRange)]

        val offset = sourceRange.start - destinationRange.start
        return value + offset
    }
}

data class Range(val start: Long, val length: Long) {
    fun isValueInRange(value: Long): Boolean {
        return value >= start && value < start + length
    }
}

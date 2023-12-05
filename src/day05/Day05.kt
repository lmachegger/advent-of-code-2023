package day05

import println
import readInput

fun main() {
    fun part1(input: List<String>): Long {
        val (seeds, mappings) = parseInput(input)

        return seeds.minOf { seed ->
            mappings.fold(seed) { acc, mapping -> mapping.mapValue(acc) }
        }
    }

    /**
     * This is a shitty brute force, that took ~5min to finish xD
     * It would be better to:
     * 1. find the lowest location
     * 2. map your way backwards
     * 3. check if the result is a valid seed
     * 4. if not, use the next lowest location
     */
    fun part2(input: List<String>): Long {
        val (seeds, mappings) = parseInput(input)
        var currentMin = Long.MAX_VALUE
        for (i in seeds.indices step 2) {
            val start = seeds[i]
            val end = start + seeds[i + 1] - 1
            val newMin = (start..end).minOf { seed ->
                var currentVal = seed
                for (mapping in mappings) {
                    currentVal = mapping.mapValue(currentVal)
                }
                currentVal
            }

            if (newMin < currentMin) {
                currentMin = newMin
            }
        }

        return currentMin
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
    var name = ""
    for (i in 2 until input.size) {
        val line = input[i]
        if (line.isEmpty() || i == input.size - 1) {
            // mapping done
            mappings.add(Mapping(name, sourceRanges.toList(), destRanges.toList()))
            sourceRanges.clear()
            destRanges.clear()
            continue
        }
        if (!line[0].isDigit()) {
            // mapping name
            name = line.substringBefore(":")
            continue
        }
        // mapping range
        val (dest, source, length) = line.split(" ").map { it.toLong() }
        sourceRanges.add(Range(source, length))
        destRanges.add(Range(dest, length))
    }


    return Pair(seeds, mappings)
}

// source to dest
data class Mapping(val name: String, val sourceRanges: List<Range>, val destinationRanges: List<Range>) {
    fun mapValue(value: Long): Long {
        val sourceRange = sourceRanges.firstOrNull { it.isValueInRange(value) } ?: return value
        val destinationRange = destinationRanges[sourceRanges.indexOf(sourceRange)]

        val offset = destinationRange.start - sourceRange.start
        return value + offset
    }
}

data class Range(val start: Long, val length: Long) {
    fun isValueInRange(value: Long): Boolean {
        return value >= start && value < start + length
    }
}

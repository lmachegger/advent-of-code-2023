package day08

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return Maps.fromInput(input).findPath()
    }

    fun part2(input: List<String>): Long {
        return Maps.fromInput(input).findPathPart2()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day08/Day08_test")

    check(part1(testInput) == 6)

    val input = readInput("day08/Day08")

    part1(input).println()
    part2(input).println()
}

data class Maps(val instructions: String, val elements: Map<String, Pair<String, String>>) {

    fun findPath(): Int {
        var steps = 0
        var key = "AAA"
        var instructionIdx = 0

        while (key != "ZZZ") {
            val instruction = instructions[instructionIdx]
            val (left, right) = elements[key]!!

            key = if (instruction == 'L') left else right

            instructionIdx = (instructionIdx + 1) % instructions.length
            steps++
        }

        return steps
    }

    fun findPathPart2(): Long {
        val startKeys = elements.keys.filter { it.endsWith("A") }

        val steps = startKeys.map {
            var key = it
            var instructionIdx = 0
            var counter = 0L
            while (!key.endsWith("Z")) {
                val instruction = instructions[instructionIdx]
                val (left, right) = elements[key]!!

                key = if (instruction == 'L') left else right

                instructionIdx = (instructionIdx + 1) % instructions.length
                counter++
            }
            counter
        }

        val min = steps.min()
        var leastCommonMultiple = min
        while (true) {
            if (steps.all { leastCommonMultiple % it == 0L }) return leastCommonMultiple
            leastCommonMultiple += min
        }
    }

    companion object {
        fun fromInput(input: List<String>): Maps {
            val instructions = input[0]
            val elements = input.subList(2, input.size).associate {
                val key = it.substring(0..<3)
                val left = it.substringAfter("(").substringBefore(",")
                val right = it.substringAfter(", ").substringBefore(")")

                key to Pair(left, right)
            }

            return Maps(instructions, elements)
        }
    }
}

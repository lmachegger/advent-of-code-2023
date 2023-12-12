package day09

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return parseInput(input).sumOf {
            it.createSequences()
            it.extrapolate()
        }

    }

    fun part2(input: List<String>): Int {
        return parseInput(input).sumOf {
            it.createSequences()
            it.extrapolateBackwards()
        }
    }

    val testInput = readInput("day09/Day09_test")
    check(part1(testInput) == 114)

    val input = readInput("day09/Day09")
    part1(input).println()
    part2(input).println()
}

fun parseInput(input: List<String>): List<Value> {
    return input.map { line -> Value(line.split(" ").map { it.toInt() }) }
}

class Value(history: List<Int>) {
    // initialize with initial history as first item
    private val sequences: MutableList<MutableList<Int>> = mutableListOf(history.toMutableList())

    fun createSequences() {
        // no need to go till all are 0, if all are same we can already stop. saves us one pointless list
        while (!sequences.last().all { it == sequences.last().first() }) {
            val sequence = mutableListOf<Int>()
            for (i in 0..<sequences.last().size - 1) {
                sequence.add(sequences.last()[i + 1] - sequences.last()[i])
            }

            this.sequences.add(sequence.toMutableList())
        }
    }

    fun extrapolate(): Int {
        for (i in sequences.size - 1 downTo 0) {
            // in the last sequence, we just copy the same value over
            if (i == sequences.size - 1) {
                sequences[i].add(sequences[i].first())
                continue
            }

            val a = sequences[i + 1].last()
            val b = sequences[i].last()
            sequences[i].add(a + b)
        }

        return sequences[0].last()
    }

    fun extrapolateBackwards(): Int {
        for (i in sequences.size - 1 downTo 0) {
            // in the last sequence, we just copy the same value over
            if (i == sequences.size - 1) {
                sequences[i].add(0, sequences[i].first())
                continue
            }

            // same as in part one, but we check the first values and subtract
            val a = sequences[i + 1].first()
            val b = sequences[i].first()
            sequences[i].add(0, b - a)
        }

        return sequences[0].first()
    }
}

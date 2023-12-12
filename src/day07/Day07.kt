package day07

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val hands = input.map { Hand.fromString(it) }
        val sorted = hands.sorted()

        var result = 0
        sorted.forEachIndexed { index, card ->
            result += ((index + 1) * card.bid)
        }

        return result
    }

    fun part2(input: List<String>): Int {
        val hands = input.map { Hand2.fromString(it) }
        val sorted = hands.sorted()

        var result = 0
        sorted.forEachIndexed { index, card ->
            result += ((index + 1) * card.bid)
        }
        return result
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day07/Day07_test")

    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("day07/Day07")

    part1(input).println()
    part2(input).println()
}

abstract class BaseHand(private val cards: String, private val mapping: Map<Char, Int>, val bid: Int) : Comparable<BaseHand> {
    private val cardValues: List<Int> = cards.map { if (it.isDigit()) it.digitToInt() else mapping[it]!! }
    private val type: HandType = findHandType(countCards())

    override fun compareTo(other: BaseHand): Int {
        return compareValuesBy(
            this, other,
            { it.type.strength },
            { it.cardValues[0] },
            { it.cardValues[1] },
            { it.cardValues[2] },
            { it.cardValues[3] },
            { it.cardValues[4] }
        )
    }

    protected open fun findHandType(occurrences: Map<Char, Int>): HandType {
        if (occurrences.values.contains(5)) return HandType.FIVE_OF_A_KIND
        if (occurrences.values.contains(4)) return HandType.FOUR_OF_A_KIND
        if (occurrences.values.contains(3) && occurrences.values.contains(2)) return HandType.FULL_HOUSE
        if (occurrences.values.contains(3)) return HandType.THREE_OF_A_KIND
        if (occurrences.values.filter { it == 2 }.size == 2) return HandType.TWO_PAIR
        if (occurrences.values.contains(2)) return HandType.ONE_PAIR

        return HandType.HIGH_CARD
    }

    private fun countCards(): Map<Char, Int> {
        val occurrences = mutableMapOf<Char, Int>()
        for (c in cards) {
            occurrences.putIfAbsent(c, 0)
            occurrences[c] = occurrences[c]!! + 1
        }
        return occurrences
    }
}

class Hand(cards: String, mapping: Map<Char, Int>, bid: Int) : BaseHand(cards, mapping, bid) {
    companion object {
        fun fromString(input: String): Hand {
            val (cards, bid) = input.split(" ")
            return (Hand(cards = cards, mapping = mapping, bid = bid.toInt()))
        }

        private val mapping = mapOf(
            'A' to 14,
            'K' to 13,
            'Q' to 12,
            'J' to 11,
            'T' to 10,
        )
    }
}

class Hand2(cards: String, mapping: Map<Char, Int>, bid: Int) : BaseHand(cards, mapping, bid) {
    override fun findHandType(occurrences: Map<Char, Int>): HandType {
        if (!occurrences.keys.contains('J') || occurrences.entries.size == 1) return super.findHandType(occurrences)

        var bestNonJoker: Map.Entry<Char, Int>? = null
        occurrences.entries.forEach {
            if (it.key != 'J' && (bestNonJoker == null || it.value > bestNonJoker!!.value)) bestNonJoker = it
        }

        val newOccurrences = mutableMapOf<Char, Int>()
        newOccurrences[bestNonJoker!!.key] = occurrences[bestNonJoker!!.key]!! + occurrences['J']!!
        occurrences.entries.forEach {
            if (it.key != 'J' && it.key != bestNonJoker!!.key) newOccurrences[it.key] = it.value
        }

        return super.findHandType(newOccurrences)
    }

    companion object {
        fun fromString(input: String): Hand2 {
            val (cards, bid) = input.split(" ")
            return (Hand2(cards = cards, mapping = mapping, bid = bid.toInt()))
        }

        private val mapping = mapOf(
            'A' to 14,
            'K' to 13,
            'Q' to 12,
            'J' to 1,
            'T' to 10,
        )
    }
}

enum class HandType(val strength: Int) {
    FIVE_OF_A_KIND(1000),
    FOUR_OF_A_KIND(900),
    FULL_HOUSE(800),
    THREE_OF_A_KIND(700),
    TWO_PAIR(600),
    ONE_PAIR(500),
    HIGH_CARD(400)
}

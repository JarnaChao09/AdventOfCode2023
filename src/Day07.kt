const val order1 = "23456789TJQKA"
const val order2 = "J23456789TQKA"

fun handPower1(hand: String): Int {
    val handType = hand.toList().distinct().associateWith {
        hand.count { c -> c == it }
    }
    return when (handType.size) {
        1 -> {
            if (handType.any { it.value == 5 }) {
                10 // 5 of a kind
            } else {
                error("unreachable")
            }
        }
        2 -> {
            if (handType.any { it.value == 4 }) {
                9 // 4 of a kind
            } else if (handType.any { it.value == 3 }) {
                if (handType.any { it.value == 2 }) {
                    8 // full house
                } else {
                    error("unreachable")
                }
            } else {
                error("unreachable")
            }
        }
        3 -> {
            if (handType.any { it.value == 3 }) {
                7 // 3 of a kind
            } else if (handType.count { it.value == 2 } == 2) {
                6 // 2 pair
            } else {
                error("unreachable")
            }
        }
        4 -> {
            if (handType.any { it.value == 2 }) {
                5 // 1 pair
            } else {
                error("unreachable")
            }
        }
        5 -> {
            4 // nothing
        }
        else -> error("unreachable")
    }
}

fun handPower2(hand: String): Int {
    val handType = hand.toList().distinct().associateWith {
        hand.count { c -> c == it }
    }
    return when (handType.size) {
        1 -> {
            if (handType.any { it.value == 5 }) {
                10 // 5 of a kind
            } else {
                error("unreachable")
            }
        }
        2 -> {
            if (handType.any { it.value == 4 } && 'J' in handType && handType['J']!! == 1) {
                10 // 5 of a kind
            } else if (handType.any { it.value == 3 } && 'J' in handType && handType['J']!! == 2) {
                10 // 5 of a kind
            } else if (handType.any { it.value == 2 } && 'J' in handType && handType['J']!! == 3) {
                10 // 5 of a kind
            } else if ('J' in handType && handType['J']!! == 4) {
                10 // 5 of a kind
            } else if (handType.any { it.value == 4 }) {
                9 // 4 of a kind
            } else if (handType.any { it.value == 3 }) {
                if (handType.any { it.value == 2 }) {
                    8 // full house
                } else {
                    error("unreachable")
                }
            } else {
                error("unreachable")
            }
        }
        3 -> {
            if (handType.any { it.value == 3 } && 'J' in handType && handType['J']!! == 1) {
                9 // 4 of a kind
            } else if (handType.count { it.value == 2 } == 2 && 'J' in handType && handType['J']!! == 1) {
                8 // full house
            } else if (handType.any { it.value == 2 } && 'J' in handType && handType['J']!! == 2) {
                9 // 4 of a kind
            } else if ('J' in handType && handType['J'] == 3) {
                9 // 4 of a kind
            } else if (handType.any { it.value == 3 }) {
                7 // 3 of a kind
            } else if (handType.count { it.value == 2 } == 2) {
                6 // 2 pair
            } else {
                error("unreachable")
            }
        }
        4 -> {
            if (handType.any { it.value == 2 } && 'J' in handType && handType['J']!! == 1) {
                7 // 3 of a kind
            } else if ('J' in handType && handType['J']!! == 2) {
                7 // 3 of a kind
            } else if (handType.any { it.value == 2 }) {
                5 // 1 pair
            } else {
                error("unreachable")
            }
        }
        5 -> {
            if ('J' in handType && handType['J']!! == 1) {
                5 // 1 pair
            } else {
                4 // nothing
            }
        }
        else -> error("unreachable")
    }
}

fun makeHandCompare(part: Int): (Pair<String, *>, Pair<String, *>) -> Int {
    val order = if (part == 1) order1 else order2
    val handPower = if (part == 1) ::handPower1 else ::handPower2
    fun handCompare(hand1Pair: Pair<String, *>, hand2Pair: Pair<String, *>): Int {
        val hand1 = hand1Pair.first
        val hand2 = hand2Pair.first
        val hand1Power = handPower(hand1)
        val hand2Power = handPower(hand2)

        val powerDifference = hand1Power - hand2Power

        if (powerDifference == 0) {
            for ((h1, h2) in hand1.toList().zip(hand2.toList())) {
                val h1p = order.indexOf(h1)
                val h2p = order.indexOf(h2)
                val hpd = h1p - h2p
                if (hpd != 0) {
                    return hpd
                }
            }
            return 0
        } else {
            return powerDifference
        }
    }

    return ::handCompare
}

fun main() {
    readInput("input/Day07").map {
        val (l, r) = it.split(" ")
        l to r.toLong()
    }.run {
        repeat(2) {
            sortedWith(makeHandCompare(it + 1)).mapIndexed { i, (_, v) ->
                (i + 1) * v
            }.sum().println()
        }
    }
}
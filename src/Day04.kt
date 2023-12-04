import kotlin.math.pow

fun main() {
    readInput("input/Day04").map {
        it.takeLastWhile { e -> e != ':' }.split(" | ").map { s ->
            s.split(" ").filter(String::isNotEmpty).map(String::toInt)
        }
    }.run {
        sumOf {
            2.0.pow(it.last().count { e ->
                e in it.first()
            } - 1).toInt()
        }.println()
        val cardMap = mutableMapOf<Int, Int>()
        for (i in indices) {
            cardMap[i] = 1
        }
        forEachIndexed { index, cards ->
            val count = cards.last().count { e ->
                e in cards.first()
            }
            repeat(count) { i ->
                repeat(cardMap[index]!!) { _ ->
                    if (index + i + 1 in indices) {
                        cardMap[index + i + 1] = cardMap[index + i + 1]!! + 1
                    }
                }
            }
        }
        cardMap.values.sum().println()
    }
}
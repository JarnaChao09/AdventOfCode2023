fun <T> List<T>.repeat(times: Int): List<T> = buildList {
    with(this@repeat) {
        repeat(times) {
            addAll(this)
        }
    }
}

fun <T> List<List<T>>.join(element: T): List<T> = buildList {
    var c = 0
    while (c < this@join.size) {
        addAll(this@join[c])
        if (c != this@join.size - 1) {
            add(element)
        }
        c++
    }
}

val cache = mutableMapOf<Pair<List<Char>, List<Int>>, Long>()

fun solutions(springs: List<Char>, brokenSprings: List<Int>): Long {
    if (springs.isEmpty()) {
        return brokenSprings.isEmpty().toInt().toLong()
    }

    return when (springs.first()) {
        '.' -> {
            solutions(springs.drop(1), brokenSprings)
        }
        '#' -> {
            helper(springs, brokenSprings).also {
                cache[springs to brokenSprings] = it
            }
        }
        '?' -> {
            solutions(springs.drop(1), brokenSprings) + helper(springs, brokenSprings).also {
                cache[springs to brokenSprings] = it
            }
        }
        else -> error("unreachable")
    }
}

fun helper(springs: List<Char>, brokenSprings: List<Int>): Long {
    cache[springs to brokenSprings]?.let { return it }

    if (brokenSprings.isEmpty()) {
        return 0
    }

    val current = brokenSprings.first()

    if (springs.size < current) {
        return 0
    }

    for (i in 0..<current) {
        if (springs[i] == '.') {
            return 0
        }
    }

    if (springs.size == current) {
        return (brokenSprings.size == 1).toLong()
    }

    if (springs[current] == '#') {
        return 0
    }

    return solutions(springs.drop(current + 1), brokenSprings.drop(1))
}

fun main() {
    readInput("input/Day12").map {
        val (s, n) = it.split(" ")

        val part1 = solutions(s.toList(), n.split(",").map(String::toInt))
        val part2 = solutions(List(5) { s.toList() }.join('?'), n.split(",").map(String::toInt).repeat(5))

        part1 to part2
    }.fold(0L to 0L) { (al, ar), (l, r) ->
        (al + l) to (ar + r)
    }.println()
}
fun main() {
    readInput("input/Day03").map(String::toList).run {
        buildMap {
            fun MutableList<Long>.perform(acc: String, row: List<Char>, locations: Set<Pair<Int, Int>>) {
                val validLocations = locations.filter { (ri, ci) ->
                    ri in this@run.indices && ci in row.indices && this@run[ri][ci] !in '0'..'9'
                }
                val condition1 = validLocations.any { (ri, ci) ->
                    this@run[ri][ci] != '.'
                }
                val condition2 = validLocations.filter { (ri, ci) ->
                    this@run[ri][ci] == '*'
                }
                if (condition1) {
                    add(acc.toLong())
                }
                condition2.forEach {
                    this@buildMap[it] = getOrElse(it) { listOf<Long>() } + listOf(acc.toLong())
                }
            }

            buildList {
                for ((r, row) in this@run.withIndex()) {
                    var locations = mutableSetOf<Pair<Int, Int>>()
                    var acc = ""
                    for ((c, element) in row.withIndex()) {
                        if (!element.isDigit()) {
                            if (acc.isNotEmpty()) {
                                perform(acc, row, locations).also {
                                    acc = ""
                                    locations = mutableSetOf()
                                }
                            }
                        } else if (element.isDigit()) {
                            acc += element
                            with(locations) {
                                add(r to (c - 1))
                                add(r to (c + 1))
                                add((r - 1) to c)
                                add((r + 1) to c)
                                add((r - 1) to (c - 1))
                                add((r - 1) to (c + 1))
                                add((r + 1) to (c - 1))
                                add((r + 1) to (c + 1))
                            }
                        }
                    }
                    if (acc.isNotEmpty()) {
                        perform(acc, row, locations)
                    }
                }
            }.sum().println()
        }.filterValues {
            it.size == 2
        }.mapValues { (_, v) ->
            v.reduce(Long::times)
        }.values.sum().println()
    }
}
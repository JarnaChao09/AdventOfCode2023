fun List<List<Char>>.calc(): Int {
    return this.withIndex().sumOf { (index, it) ->
        it.count { e -> e == 'O' } * (this.size - index)
    }
}

fun List<List<Char>>.moveWest(): List<List<Char>> {
    val ret = MutableList(this.size) { MutableList(this[it].size) { '.' } }
    this.forEachIndexed { i, row ->
        var lastIndex = 0
        row.forEachIndexed { j, c ->
            if (c == 'O') {
                ret[i][lastIndex++] = 'O'
            } else if (c == '#') {
                ret[i][j] = '#'
                lastIndex = j + 1
            }
        }
    }

    return ret.map(MutableList<Char>::toList).toList()
}

fun List<List<Char>>.fullCycle(): List<List<Char>> {
    val north = this.transpose().moveWest().transpose()
    val west = north.moveWest()
    val south = west.transpose().map(List<Char>::reversed).moveWest().map(List<Char>::reversed).transpose()
    val east = south.map(List<Char>::reversed).moveWest().map(List<Char>::reversed)

    return east.toList()
}

fun main() {
    readInput("input/Day14").map {
        it.toList()
    }.run {
        val part1 = this.transpose().moveWest().transpose().calc()

        val seen = mutableMapOf<String, Int>()
        var grid = this.toList()
        var cycle = 0
        val total = 1_000_000_000
        var length = 0

        while (cycle < total) {
            val curr = grid.joinToString(separator = "") {
                it.joinToString(separator = "")
            }
            if (curr in seen) {
                length = cycle - seen[curr]!!
                break
            }

            seen[curr] = cycle

            grid = grid.fullCycle()

            cycle++
        }

        if (length > 0) {
            val remainingCycles = (total - cycle) % length
            for (i in 0..<remainingCycles) {
                grid = grid.fullCycle()
            }
        }

        val part2 = grid.calc()

        part1 to part2
    }.println()
}
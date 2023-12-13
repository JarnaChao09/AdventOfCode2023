fun <T : Collection<*>> List<T>.splitOnEmpty(): List<List<T>> = buildList {
    var tmp = mutableListOf<T>()
    this@splitOnEmpty.forEach {
        if (it.isNotEmpty()) {
            tmp.add(it)
        } else {
            add(tmp.toList())
            tmp = mutableListOf()
        }
    }
    if (tmp.isNotEmpty()) {
        add(tmp.toList())
    }
}

fun <T> List<List<T>>.check(exclude: Int = -1): Int {
    val ret = this.indices.toList().dropLast(1).firstOrNull {
        if (it + 1 == exclude) return@firstOrNull false
        this.indices.toList().all { o ->
            val other = it * 2 + 1 - o
            if (other !in this.indices) {
                true
            } else {
                this[o] == this[other]
            }
        }
    }?.let { it + 1 } ?: 0

    return ret
}

fun main() {
    readInput("input/Day13").map {
        it.toList()
    }.splitOnEmpty().map {
        fun Char.swap(): Char = when (this) {
            '#' -> '.'
            '.' -> '#'
            else -> this
        }

        val rowIndex = it.check()
        val colIndex = it.transpose().check()

        for (i in it.indices) {
            for (j in it[0].indices) {
                val newMirror = it.map(List<Char>::toMutableList).toMutableList().run {
                    this[i][j] = this[i][j].swap()
                    this.map(List<Char>::toList).toList()
                }

                val newRowIndex = newMirror.check(rowIndex)
                val newColIndex = newMirror.transpose().check(colIndex)

                if (newRowIndex == 0 && newColIndex == 0) continue

                return@map (rowIndex * 100 + colIndex) to (newRowIndex * 100 + newColIndex)
            }
        }

        error("unreachable")
    }.fold(0 to 0) { (al, ar), (l, r) ->
        (al + l) to (ar + r)
    }.println()
}
import java.util.PriorityQueue

data class Point(val r: Int, val c: Int, val direction: Int, val currentLength: Int, val score: Int)

val directions: List<Direction> = listOf(Direction.Down, Direction.Right, Direction.Up, Direction.Left)

fun bestPath(grid: List<List<Char>>, minLen: Int, maxLen: Int): Int {
    fun Char.digitToInt(): Int = this.code - '0'.code

    val rows = grid.indices
    val cols = grid[0].indices

    val best = mutableMapOf<Point, Int>()

    val queue = PriorityQueue<Point>(Comparator.comparing { it.score })
    for (direction in directions.indices) {
        with(Point(0, 0, direction, 0, 0)) {
            queue.add(this)
            best[this] = 0
        }
    }

    var ret = Int.MAX_VALUE

    while (queue.isNotEmpty()) {
        val current = queue.remove()

        if (current.score > ret) {
            break
        }

        if (current.currentLength < maxLen) {
            val nr = current.r + directions[current.direction].r
            val nc = current.c + directions[current.direction].c

            if (nr in rows && nc in cols) {
                val newScore = current.score + grid[nr][nc].digitToInt()
                if (nr == rows.last && nc == cols.last) {
                    ret = minOf(ret, newScore)
                }

                fun tryPoint(point: Point) {
                    if (point.score > ret) {
                        return
                    }

                    val c = point.copy(score = 0)

                    val curr = best[c]
                    if (curr == null || curr > newScore) {
                        best[c] = newScore
                        queue.add(point)
                    }
                }

                tryPoint(Point(nr, nc, current.direction, current.currentLength + 1, newScore))

                if (current.currentLength + 1 >= minLen) {
                    tryPoint(Point(nr, nc, (current.direction + 1) % 4, 0, newScore))
                    tryPoint(Point(nr, nc, (current.direction + 3) % 4, 0, newScore))
                }
            }
        }
    }

    return ret
}

fun main() {
    val grid = readInput("input/Day17").map {
        it.toList()
    }

    val part1 = bestPath(grid, 1, 3)
    println(part1)

    val part2 = bestPath(grid, 4, 10)
    println(part2)
}
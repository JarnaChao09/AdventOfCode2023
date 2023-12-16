enum class Direction(val r: Int, val c: Int) {
    Up(-1, 0),
    Right(0, +1),
    Down(+1, 0),
    Left(0, -1);

    operator fun component1(): Int = this.r

    operator fun component2(): Int = this.c

    fun changeDirection(onChar: Char): Pair<Direction, Direction?> {
        return when (onChar) {
            '.' -> this to null
            '/' -> {
                when (this) {
                    Up -> Right to null
                    Right -> Up to null
                    Down -> Left to null
                    Left -> Down to null
                }
            }
            '\\' -> {
                when (this) {
                    Up -> Left to null
                    Right -> Down to null
                    Down -> Right to null
                    Left -> Up to null
                }
            }
            '-' -> {
                when (this) {
                    Up -> Left to Right
                    Right -> Right to null
                    Down -> Left to Right
                    Left -> Left to null
                }
            }
            '|' -> {
                when (this) {
                    Up -> Up to null
                    Right -> Up to Down
                    Down -> Down to null
                    Left -> Up to Down
                }
            }
            else -> error("invalid character $onChar")
        }
    }
}

operator fun Pair<Int, Int>.plus(direction: Direction): Pair<Int, Int> {
    val (cr, cc) = this
    val (dr, dc) = direction
    return (cr + dr) to (cc + dc)
}

operator fun Direction.plus(point: Pair<Int, Int>): Pair<Int, Int> = point + this

operator fun <T> List<List<T>>.get(point: Pair<Int, Int>): T? =
    if (point.first in this.indices && point.second in this[point.first].indices) {
        this[point.first][point.second]
    } else {
        null
    }

fun <T> ArrayDeque<T>.drainFirst(block: (T) -> Unit) {
    while (this.isNotEmpty()) {
        block(this.removeFirst())
    }
}

fun <T> dequeOf(vararg items: T): ArrayDeque<T> = ArrayDeque(items.toList())
fun <T> dequeOf(items: List<T>): ArrayDeque<T> = ArrayDeque(items.toList())

fun run(grid: List<List<Char>>, startPoint: Pair<Int, Int>, startDirection: Direction): Set<Pair<Int, Int>> {
    val seen = mutableSetOf<Pair<Pair<Int, Int>, Direction>>()

    val queue = dequeOf(startPoint to startDirection)

    queue.drainFirst { (pos, dir) ->
        if (pos to dir in seen) {
            return@drainFirst
        }
        seen += pos to dir

        val (d1, d2) = dir.changeDirection(grid[pos]!!)
        grid[pos + d1]?.let {
            queue.addLast((pos + d1) to d1)
        }
        d2?.let { d ->
            grid[pos + d]?.let {
                queue.addLast((pos + d) to d)
            }
        }
    }

    return seen.map { it.first }.toSet()
}

fun main() {
    val grid = readInput("input/Day16").map {
        it.toList()
    }

    val part1 = run(grid, 0 to 0, Direction.Right)

    println(part1.size)

    val part2 = listOf(
        grid[0].indices.map { grid.indices.last to it }.map { it to Direction.Up },
        grid.indices.map { it to 0 }.map { it to Direction.Right },
        grid[0].indices.map { 0 to it }.map { it to Direction.Down },
        grid.indices.map { it to grid[0].indices.last }.map { it to Direction.Left}
    ).flatMap {
        it.map { (start, direction) ->
            run(grid, start, direction).size
        }
    }.max()

    println(part2)
}
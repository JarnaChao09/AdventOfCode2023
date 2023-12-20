fun Direction.changeDirection(onChar: Char): Pair<Direction, Direction?> {
    return when (onChar) {
        '.' -> this to null
        '/' -> {
            when (this) {
                Direction.Up -> Direction.Right to null
                Direction.Right -> Direction.Up to null
                Direction.Down -> Direction.Left to null
                Direction.Left -> Direction.Down to null
            }
        }
        '\\' -> {
            when (this) {
                Direction.Up -> Direction.Left to null
                Direction.Right -> Direction.Down to null
                Direction.Down -> Direction.Right to null
                Direction.Left -> Direction.Up to null
            }
        }
        '-' -> {
            when (this) {
                Direction.Up -> Direction.Left to Direction.Right
                Direction.Right -> Direction.Right to null
                Direction.Down -> Direction.Left to Direction.Right
                Direction.Left -> Direction.Left to null
            }
        }
        '|' -> {
            when (this) {
                Direction.Up -> Direction.Up to null
                Direction.Right -> Direction.Up to Direction.Down
                Direction.Down -> Direction.Down to null
                Direction.Left -> Direction.Up to Direction.Down
            }
        }
        else -> error("invalid character $onChar")
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
            addLast((pos + d1) to d1)
        }
        d2?.let { d ->
            grid[pos + d]?.let {
                addLast((pos + d) to d)
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
import kotlin.math.absoluteValue

fun calcArea(points: Set<Pair<Long, Long>>): Long {
    var area = 0L
    for ((curr, next) in points.zipWithNext()) {
        val (pr, pc) = curr
        val (nr, nc) = next
        area += (pr - nr) * (pc + nc)
    }
    return area.absoluteValue / 2
}

fun main() {
    val input = readInput("input/Day18").map {
        it.split(" ")
    }

    var startingPos1 = 0L to 0L
    val dug1 = mutableSetOf(startingPos1)
    var perimeter1 = 0L

    var startingPos2 = 0L to 0L
    val dug2 = mutableSetOf(startingPos2)
    var perimeter2 = 0L

    val directions = listOf(Direction.Right, Direction.Down, Direction.Left, Direction.Up)

    for (i in input) {
        val (dir, dist, color) = i
        val (r, c) = when (dir) {
            "R" -> Direction.Right
            "D" -> Direction.Down
            "L" -> Direction.Left
            "U" -> Direction.Up
            else -> error("invalid direction")
        }
        val distance = dist.toLong()
        startingPos1 = (startingPos1.first + distance * r) to (startingPos1.second + distance * c)
        perimeter1 += distance
        dug1.add(startingPos1)

        val code = color.drop(2).dropLast(1)
        val codeDistance = code.take(5).toLong(radix = 16)
        val (cdr, cdc) = directions[code.takeLast(1).toInt()]

        startingPos2 = (startingPos2.first + codeDistance * cdr) to (startingPos2.second + codeDistance * cdc)
        perimeter2 += codeDistance
        dug2.add(startingPos2)
    }

    // according to the formula, calcArea(dug + listOf(0L to 0L)) is correct but for the reasons stated below, it is not
    // needed
    // shoelace always needs to "tie the ends together" but since it is 0, 0 and to get back to 0, 0, the column must
    // be 0 as well, we can leave it off in the calculation
    //
    // furthermore, to correct for the area calculated assuming lines at those coordinates instead of squares
    // half of those 1m by 1m squares are not included in the area calculation (assuming that the points are at the
    // center of this square), therefore we need to add half of the perimeter + 1 (the 1 represents the 4 corners which
    // instead of having half of the square outside the area, instead have 3/4s outside the area)
    println(calcArea(dug1) + perimeter1 / 2 + 1)
    println(calcArea(dug2) + perimeter2 / 2 + 1)
}
import kotlin.math.absoluteValue

enum class Connections {
    NS, // |
    EW, // -
    NE, // L
    NW, // J
    SW, // 7
    SE, // F
    None, // .
    Start, // S
}

val map = mapOf(
    '|' to Connections.NS,
    '-' to Connections.EW,
    'L' to Connections.NE,
    'J' to Connections.NW,
    '7' to Connections.SW,
    'F' to Connections.SE,
    '.' to Connections.None,
    'S' to Connections.Start,
)

fun connectedLocations(grid: List<List<Connections>>, location: Pair<Int, Int>): Pair<Pair<Int, Int>, Pair<Int, Int>>? {
    val (r, c) = location
    if (r !in grid.indices || c !in grid[r].indices) {
        return null
    }

    val connection = grid[r][c]
    val north = (r - 1) to c
    val south = (r + 1) to c
    val east = r to (c + 1)
    val west = r to (c - 1)
    return when (connection) {
        Connections.NS -> north to south
        Connections.EW -> east to west
        Connections.NE -> north to east
        Connections.NW -> north to west
        Connections.SW -> south to west
        Connections.SE -> south to east
        Connections.None -> null
        Connections.Start -> (r to c) to (r to c)
    }
}

operator fun <T> Pair<T, T>.contains(thing: T): Boolean = first == thing || second == thing

fun main() {
    var startLocation: Pair<Int, Int> = 0 to 0
    val grid = readInput("input/Day10").mapIndexed { r, it ->
        it.map { c -> map[c]!! }.also { l ->
            if (Connections.Start in l)
                startLocation = r to l.indexOf(Connections.Start)
        }
    }

    val visited = mutableSetOf(startLocation)
    var startType: Connections = Connections.Start
    var currentLocations: List<Pair<Pair<Int, Int>, Int>> = buildList {
        val (sr, sc) = startLocation
        var typeNS = 0
        val north = connectedLocations(grid, (sr - 1) to sc)
        if (north != null && startLocation in north) {
            typeNS = 1
            add(((sr - 1) to sc) to 1)
        }

        val south = connectedLocations(grid, (sr + 1) to sc)
        if (south != null && startLocation in south) {
            typeNS = if (typeNS == 1) 3 else 2
            add(((sr + 1) to sc) to 1)
        }

        var typeEW = 0
        val east = connectedLocations(grid, sr to (sc - 1))
        if (east != null && startLocation in east) {
            typeEW = 1
            add((sr to (sc - 1)) to 1)
        }

        val west = connectedLocations(grid, sr to (sc + 1))
        if (west != null && startLocation in west) {
            typeEW = if (typeEW == 1) 3 else 2
            add((sr to (sc + 1)) to 1)
        }

        if (typeNS == 3) {
            startType = Connections.NS
        } else if (typeEW == 3) {
            startType = Connections.EW
        } else if (typeNS == 1) {
            startType = if (typeEW == 1) Connections.NE else Connections.NW
        } else if (typeNS == 2) {
            startType = if (typeEW == 1) Connections.SE else Connections.SW
        }
    }
    var currentLocations2 = currentLocations.drop(1).map { it.first }

    val distances = mutableMapOf<Pair<Int, Int>, Int>()
    while (currentLocations.isNotEmpty()) {
        currentLocations = buildList {
            for ((location, distance) in currentLocations) {
                visited.add(location)
                val locations = connectedLocations(grid, location)
                if (locations != null) {
                    val (nextLocation1, nextLocation2) = locations
                    if (nextLocation1 !in visited) {
                        add(nextLocation1 to (distance + 1))
                        distances[nextLocation1] = distance + 1
                    }
                    if (nextLocation2 !in visited) {
                        add(nextLocation2 to (distance + 1))
                        distances[nextLocation2] = distance + 1
                    }
                }
            }
        }
    }
    distances.maxOf { it.value }.println()

    val visited2 = mutableSetOf(startLocation)
    var (py, px) = startLocation
    var area = 0
    while (currentLocations2.isNotEmpty()) {
        currentLocations2 = buildList {
            for (location in currentLocations2) {
                visited2.add(location)
                val locations = connectedLocations(grid, location)
                if (locations != null) {
                    val (nextLocation1, nextLocation2) = locations
                    if (nextLocation1 !in visited2) {
                        add(nextLocation1)
                        val (cy, cx) = nextLocation1
                        area += (py - cy) * (px + cx)
                        py = cy
                        px = cx
                    }
                    if (nextLocation2 !in visited2) {
                        add(nextLocation2)
                        val (cy, cx) = nextLocation2
                        area += (py - cy) * (px + cx)
                        py = cy
                        px = cx
                    }
                }
            }
        }
    }
    area += (py - startLocation.first) * (px + startLocation.second)
    println(area.absoluteValue / 2 - visited2.size / 2 + 1)

//    reference:
//    https://www.reddit.com/r/adventofcode/comments/18evyu9/comment/kcqnqql/
//
//    val revMap = mapOf(
//        Connections.NS to '|',
//        Connections.EW to '-',
//        Connections.NE to 'L',
//        Connections.NW to 'J',
//        Connections.SW to '7',
//        Connections.SE to 'F',
//        Connections.None to '.',
//        Connections.Start to 'S',
//    )
//
//    val dirs = 4
//    val dRow = listOf(-1, 0, +1, 0)
//    val dCol = listOf(0, +1, 0, -1)
//    val dNames = listOf("SLJ|", "SLF-", "S7F|", "S7J-")
//
//    val vis = List(grid.size) { MutableList(grid[it].size) { false } }
//
//    var area = 0
//    var pRow = 0
//    var pCol = 0
//
//    val go = DeepRecursiveFunction<Pair<Int, Int>, Unit> {
//        val (row, col) = it
//        if (vis[row][col]) {
//            return@DeepRecursiveFunction
//        }
//        vis[row][col] = true
//        area += (pRow - row) * (pCol + col)
//        pRow = row
//        pCol = col
//        for (dir in 0..<dirs) {
//            if (revMap[grid[row][col]]!! in dNames[dir]) {
//                val nRow = row + dRow[dir]
//                val nCol = col + dCol[dir]
//                if (nRow in grid.indices && nCol in grid[nRow].indices && revMap[grid[nRow][nCol]]!! in dNames[dir xor 2]) {
//                    callRecursive(nRow to nCol)
//                }
//            }
//        }
//    }
//
//    pRow = startLocation.first
//    pCol = startLocation.second
//    go(startLocation)
//    area += (pRow - startLocation.first) * (pCol + startLocation.second)
//    val border = vis.flatten().sumOf { it.toInt() }
//    println(area.absoluteValue / 2 - border / 2 + 1)
}
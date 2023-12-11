import kotlin.math.absoluteValue

typealias Grid<T> = List<List<T>>

fun <T> Grid<T>.transpose(): Grid<T> = List(this[0].size) { r -> List(this.size) { c -> this[c][r] } }

fun <T> Grid<T>.toIndexed(): Grid<Triple<T, Int, Int>> = this.mapIndexed { r, e -> e.mapIndexed { c, v -> Triple(v, r, c) }}

fun <T> Grid<T>.toIndexedLong(): Grid<Triple<T, Long, Long>> = this.mapIndexed { r, e -> e.mapIndexed { c, v -> Triple(v, r.toLong(), c.toLong()) }}

fun <T> Iterable<T>.pairWise(): List<Pair<T, T>> = this.flatMapIndexed { i, v -> this.drop(i + 1).map { v to it } }

fun Iterable<Int>.interval(): IntRange = this.min() .. this.max()

fun Iterable<Long>.interval(): LongRange = this.min() .. this.max()

fun main() {
    val data = readInput("input/Day11")
    val data1 = data.flatMap {
        if (it.all { c -> c == '.' }) listOf(it.toList(), it.toList()) else listOf(it.toList())
    }.transpose().flatMap {
        if (it.all { c -> c == '.'}) listOf(it.toList(), it.toList()) else listOf(it.toList())
    }.transpose()

    data1.toIndexed().flatten().filter { it.first == '#' }.map { (_, r, c) -> r to c }.pairWise().sumOf {
        val (l, r) = it
        val (ly, lx) = l
        val (ry, rx) = r

        (ly - ry).absoluteValue + (lx - rx).absoluteValue
    }.println()

    val data2 = data.map {
        it.toList()
    }.toIndexedLong()

    val rows = data2.indices.map(Int::toLong).toMutableSet()
    val cols = data2[0].indices.map(Int::toLong).toMutableSet()

    with(data2.flatten().filter { it.first == '#' }) {

        for ((_, r, c) in this) {
            rows.remove(r)
            cols.remove(c)
        }

        map { (_, r, c) -> r to c }.pairWise().sumOf { pair ->
            val (l, r) = pair
            val (ly, lx) = l
            val (ry, rx) = r

            val manDist = (ly - ry).absoluteValue + (lx - rx).absoluteValue

            val expansionsOnY = listOf(ly, ry).interval().count { it in rows }
            val expansionsOnX = listOf(lx, rx).interval().count{ it in cols }

            manDist + (expansionsOnX + expansionsOnY) * (1000000 - 1)
        }.println()
    }

}
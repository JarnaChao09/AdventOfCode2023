import kotlin.io.path.Path
import kotlin.io.path.readText

fun main() {
    val input = Path("src/input/Day05.txt").readText().split("\n\n")
    var seeds = input[0].takeLastWhile { it != ':' }.split(" ").filter(String::isNotEmpty).map(String::toLong)
    val seeds2 = seeds.toList()
    repeat(7) {
        seeds = seeds.map { seed ->
            var acc = seed
            for (s in input[it + 1].split("\n").drop(1)) {
                val (destStart, sourceStart, rangeSize) = s.split(" ").filter(String::isNotEmpty)
                    .map(String::toLong)
                if (seed in sourceStart..<sourceStart+rangeSize) {
                    acc = destStart + (seed - sourceStart)
                    break
                }
            }
            acc
        }
    }
    seeds.min().println()
    val input2 = input.drop(1).reversed()
    var location = 0L
    var found = false
    while (!found) {
        var acc = location
        repeat(7) {
            for (s in input2[it].split("\n").drop(1)) {
                val (destStart, sourceStart, rangeSize) = s.split(" ").filter(String::isNotEmpty)
                    .map(String::toLong)
                if (acc in destStart..<destStart + rangeSize) {
                    acc = sourceStart + (acc - destStart)
                    break
                }
            }
        }
        for ((s, r) in seeds2.chunked(2)) {
            if (acc in s..<s+r) {
                found = true
            }
        }
        location++
    }
    println(location - 1)
}
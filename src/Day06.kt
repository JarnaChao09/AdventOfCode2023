fun main() {
    val (time, dist) = readInput("input/Day06").map {
        it.takeLastWhile { e -> e != ':' }.split(" ").filter(String::isNotEmpty)
    }

    time.map(String::toLong).zip(dist.map(String::toLong)).map { (t, d) ->
        var c = 0L
        for (it in 0..<t + 1) {
            if ((t - it) * it > d) {
                c++
            }
        }
        c
    }.reduce(Long::times).println()

    val totalTime = time.joinToString(separator = "").toLong()
    val totalDist = dist.joinToString(separator = "").toLong()

    var c = 0L
    for (it in 0..<totalTime+1) {
        if ((totalTime - it) * it > totalDist) {
            c++
        }
    }
    println(c)
}
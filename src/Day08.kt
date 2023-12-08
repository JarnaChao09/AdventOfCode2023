fun main() {
    fun gcd(a: Long, b: Long): Long = if (a == 0L) b else gcd(b % a, a)

    val funcs = mapOf(
        'L' to Pair<String, String>::first,
        'R' to Pair<String, String>::second,
    )

    fun run(current: String, instructions: List<Char>, map: Map<String, Pair<String, String>>, stop: (String) -> Boolean): Long {
        var curr = current
        var i = 0
        var c = 0L

        while (!stop(curr)) {
            curr = funcs[instructions[i]]!!(map[curr]!!)
            i = (i + 1).takeIf { it != instructions.size } ?: 0
            c++
        }

        return c
    }

    val input = readInput("input/Day08")
    val instructions = input.first().toList()
    val map = buildMap {
        input.drop(2).forEach {
            val (k, l, r) = it.replace(Regex("[^A-Za-z ]"), "").split(Regex(" +"))
            put(k, l to r)
        }
    }

    println(run("AAA", instructions, map) { it == "ZZZ" })

    map.keys.filter { it.endsWith("A") }.map {
        run(it, instructions, map) { s -> s.endsWith("Z") }
    }.reduce { acc, it ->
        acc / gcd(acc, it) * it
    }.println()
}
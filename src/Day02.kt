fun main() {
    readInput("input/Day02").foldIndexed(0 to 0) { index, acc, it ->
        val maps = it.takeLastWhile { e -> e != ':' }.split("; ").map { s ->
            s.split(", ").map { e -> e.split(" ").filter(String::isNotEmpty) }.map { l ->
                l.first() to l.last()
            }.groupBy { p ->
                p.second
            }
        }
        val map = buildMap {
            maps.forEach {
                it.keys.forEach { key ->
                    this[key] = (listOf(getOrElse(key) { 0 }) + it[key]!!.map { (v, _) ->
                        v.toInt()
                    }).max()
                }
            }
        }
        val condition = with(map) {
            getOrElse("red") { 0 } <= 12 &&
                    getOrElse("green") { 0 } <= 13 &&
                    getOrElse("blue") { 0 } <= 14
        }
        (acc.first + if (condition) {
            index + 1
        } else {
            0
        }) to (acc.second + with(map) {
            getOrElse("red") { 0 } * getOrElse("green") { 0 } * getOrElse("blue") { 0 }
        })
    }.println()
}
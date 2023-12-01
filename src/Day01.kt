fun main() {
    readInput("input/Day01").sumOf {
        it.split(Regex("\\D")).filter(String::isNotEmpty).flatMap(String::toList).run {
            "${first()}${last()}".toInt()
        }
    }.println()
    readInput("input/Day01").sumOf {
        buildList {
            it.withIndex().forEach { (index, c) ->
                if (c in '0'..'9') {
                    add(c - '0')
                }
                if (index + 3 <= it.length) {
                    when (it.slice(index..<index + 3)) {
                        "one" -> add(1)
                        "two" -> add(2)
                        "six" -> add(6)
                    }
                }
                if (index + 4 <= it.length) {
                    when (it.slice(index..<index+4)) {
                        "four" -> add(4)
                        "five" -> add(5)
                        "nine" -> add(9)
                    }
                }
                if (index + 5 <= it.length) {
                    when (it.slice(index..<index+5)) {
                        "three" -> add(3)
                        "seven" -> add(7)
                        "eight" -> add(8)
                    }
                }
            }
        }.let {
            "${it.first()}${it.last()}".toInt()
        }
    }.println()
}
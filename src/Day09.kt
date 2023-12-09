fun main() {
    readInput("input/Day09").map {
        val current = it.split(" ").map(String::toLong)
        val first = mutableListOf(current.first())
        val last = mutableListOf(current.last())
        var curr = current.toList()
        var done1 = false

        while (!done1) {
            curr = curr.zipWithNext().map { (l, r) -> r - l }
            first.add(curr.first())
            last.add(curr.last())
            done1 = curr.all { a -> a == 0L }
        }

        last.sum() to first.first() - first.drop(1).reversed().reduce { acc, e -> e - acc }
    }.reduce { (al, ar), (l, r) ->
        (al + l) to (ar + r)
    }.println()
}
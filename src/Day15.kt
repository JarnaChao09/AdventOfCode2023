enum class OperationType { Minus, Equal, }

fun main() {
    val input = readInput("input/Day15").first().split(",")

    val part1 = input.sumOf {
        var currentValue = 0
        it.forEach { c ->
            currentValue += c.code
            currentValue = (currentValue * 17) % 256
        }

        currentValue
    }
    println(part1)

    val boxes = Array(256) { mutableMapOf<String, Int>() }
    input.forEach {
        val (label, op, value) = if ('-' in it) {
            Triple(it.dropLast(1), OperationType.Minus, -1)
        } else {
            val (label, value) = it.split("=")
            Triple(label, OperationType.Equal, value.toInt())
        }

        var currentValue = 0
        label.forEach { c ->
            currentValue += c.code
            currentValue = (currentValue * 17) % 256
        }

        when (op) {
            OperationType.Minus -> {
                if (label in boxes[currentValue]) {
                    boxes[currentValue].remove(label)
                }
            }
            OperationType.Equal -> {
                boxes[currentValue][label] = value
            }
        }
    }

    boxes.withIndex().sumOf { (index, box) ->
        var sum = 0
        var i = 0
        for ((k, v) in box) {
            sum += (index + 1) * (i + 1) * v
            i++
        }

        sum
    }.println()
}
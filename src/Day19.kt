data class Part(val x: Int, val m: Int, val a: Int, val s: Int) {
    fun rating(): Int = x + m + a + s
}

enum class Type { X, M, A, S }

enum class OpType { Lt, Gt }

sealed interface Destination

data object Reject : Destination

data object Accept : Destination

data class NewRule(val dest: String) : Destination

data class SubRule(val type: Type, val value: Int, val opType: OpType, val operation: (Int, Int) -> Boolean, val destination: Destination) {
    operator fun invoke(v: Int): Boolean = operation(v, value)
}

data class Rule(val subRules: List<SubRule>, val otherwise: Destination) {
    fun applyRule(part: Part): Destination {
        for (subRule in subRules) {
            val valueToPass = when (subRule.type) {
                Type.X -> part.x
                Type.M -> part.m
                Type.A -> part.a
                Type.S -> part.s
            }

            if (subRule(valueToPass)) {
                return subRule.destination
            }
        }

        return otherwise
    }
}

data class Quad<T>(val first: T, val second: T, val third: T, val fourth: T)

fun <T, R> Quad<T>.map(f: (T) -> R): Quad<R> = Quad(f(first), f(second), f(third), f(fourth))

fun <T> Quad<T>.any(f: (T) -> Boolean): Boolean = f(first) || f(second) || f(third) || f(fourth)

fun Quad<Long>.product(): Long = first * second * third * fourth

fun Set<Long>.toRange(): LongRange = min()..max()

val LongRange.size: Long
    get() = last - first + 1

fun eval(rulesMap: Map<String, Rule>, part: Quad<LongRange>, rule: Destination = NewRule("in")): Long {
    if (rule is Accept) {
        return part.map { it.size }.product()
    }
    if (rule is Reject) {
        return 0L
    }
    val ruleDest = rulesMap[(rule as NewRule).dest] ?: error(rule.dest)
    var cum = 0L
    var currentPart = part.copy()

    for (subRule in ruleDest.subRules) {
        if (currentPart.any { it.isEmpty() }) {
            return 0L
        }

        val type = subRule.type
        val opType = subRule.opType
        val value = subRule.value.toLong()
        val res = subRule.destination

        when (opType) {
            OpType.Lt -> {
                when (type) {
                    Type.X -> {
                        val first1 = currentPart.first.intersect(0L..(value - 1)).toRange()
                        val first2 = currentPart.first.intersect(value..4000L).toRange()
                        cum += eval(rulesMap, currentPart.copy(first = first1), res)
                        currentPart = currentPart.copy(first = first2)
                    }
                    Type.M -> {
                        val second1 = currentPart.second.intersect(0L..(value - 1)).toRange()
                        val second2 = currentPart.second.intersect(value..4000L).toRange()
                        cum += eval(rulesMap, currentPart.copy(second = second1), res)
                        currentPart = currentPart.copy(second = second2)
                    }
                    Type.A -> {
                        val third1 = currentPart.third.intersect(0L..(value - 1)).toRange()
                        val third2 = currentPart.third.intersect(value..4000L).toRange()
                        cum += eval(rulesMap, currentPart.copy(third = third1), res)
                        currentPart = currentPart.copy(third = third2)
                    }
                    Type.S -> {
                        val fourth1 = currentPart.fourth.intersect(0L..(value - 1)).toRange()
                        val fourth2 = currentPart.fourth.intersect(value..4000L).toRange()
                        cum += eval(rulesMap, currentPart.copy(fourth = fourth1), res)
                        currentPart = currentPart.copy(fourth = fourth2)
                    }
                }
            }
            OpType.Gt -> {
                when (type) {
                    Type.X -> {
                        val first1 = currentPart.first.intersect((value + 1)..4000L).toRange()
                        val first2 = currentPart.first.intersect(0..value).toRange()
                        cum += eval(rulesMap, currentPart.copy(first = first1), res)
                        currentPart = currentPart.copy(first = first2)
                    }
                    Type.M -> {
                        val second1 = currentPart.second.intersect((value + 1)..4000L).toRange()
                        val second2 = currentPart.second.intersect(0..value).toRange()
                        cum += eval(rulesMap, currentPart.copy(second = second1), res)
                        currentPart = currentPart.copy(second = second2)
                    }
                    Type.A -> {
                        val third1 = currentPart.third.intersect((value + 1)..4000L).toRange()
                        val third2 = currentPart.third.intersect(0..value).toRange()
                        cum += eval(rulesMap, currentPart.copy(third = third1), res)
                        currentPart = currentPart.copy(third = third2)
                    }
                    Type.S -> {
                        val fourth1 = currentPart.fourth.intersect((value + 1)..4000L).toRange()
                        val fourth2 = currentPart.fourth.intersect(0..value).toRange()
                        cum += eval(rulesMap, currentPart.copy(fourth = fourth1), res)
                        currentPart = currentPart.copy(fourth = fourth2)
                    }
                }
            }
        }
    }

    cum += eval(rulesMap, currentPart, ruleDest.otherwise)

    return cum
}

fun main() {
    val (rules, parts) = readInput("input/Day19").map {
        it.toList()
    }.splitOnEmpty().map {
        it.map { l -> l.joinToString(separator = "")}
    }

    val rulesMap = buildMap {
        rules.forEach {
            val (label, w) = it.dropLast(1).split("{")
            val workflow = w.split(",")
            val subRules = mutableListOf<SubRule>()
            val destination = workflow.last().let { o ->
                when (o) {
                    "A" -> Accept
                    "R" -> Reject
                    else -> NewRule(o)
                }
            }
            workflow.dropLast(1).forEach { s ->
                val (operation, dest) = s.split(":")
                val (subpart, compareValue) = operation.split(Regex("[><]"))
                val (op: (Int, Int) -> Boolean, opType: OpType) = if (">" in operation) {
                    { l: Int, r: Int -> l > r } to OpType.Gt
                } else {
                    { l: Int, r: Int -> l < r } to OpType.Lt
                }
                val ruleDest = when (dest) {
                    "A" -> Accept
                    "R" -> Reject
                    else -> NewRule(dest)
                }
                val ruleType = when (subpart) {
                    "x" -> {
                        Type.X
                    }
                    "m" -> {
                        Type.M
                    }
                    "a" -> {
                        Type.A
                    }
                    "s" -> {
                        Type.S
                    }
                    else -> error("illegal subpart")
                }

                subRules.add(SubRule(type = ruleType, value = compareValue.toInt(), opType = opType, operation = op, destination = ruleDest))
            }

            put(label, Rule(subRules, otherwise = destination))
        }
    }

    val accepted = mutableSetOf<Part>()

    parts.map {
        val (x, m, a, s) = it
            .drop(1).dropLast(1)
            .split(",")
            .map { s ->
                s.split("=")[1].toInt()
            }

        Part(x, m, a, s)
    }.forEach {
        var dest = rulesMap["in"]!!.applyRule(it)

        while (dest !is Accept && dest !is Reject) {
            dest = rulesMap[(dest as NewRule).dest]!!.applyRule(it)
        }

        if (dest is Accept) {
            accepted.add(it)
        }
    }

    accepted.sumOf(Part::rating).println()

    eval(rulesMap, Quad(1L..4000L, 1L..4000L, 1L..4000L, 1L..4000L)).println()
}
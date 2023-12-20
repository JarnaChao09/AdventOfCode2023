enum class Pulse { LOW, HIGH }

sealed interface Module {
    val destinations: List<String>

    fun sendPulse(received: Pulse, from: String): Pulse?
}

data class Broadcaster(override val destinations: List<String>) : Module {
    override fun sendPulse(received: Pulse, from: String): Pulse = received
}

data class FlipFlop(override val destinations: List<String>, var on: Boolean = false) : Module {
    override fun sendPulse(received: Pulse, from: String): Pulse? = when (received) {
        Pulse.LOW -> {
            on = !on
            Pulse.entries[on.toInt()]
        }
        Pulse.HIGH -> {
            null
        }
    }
}

data class Conjunction(override val destinations: List<String>, val memory: MutableMap<String, Pulse> = mutableMapOf()) : Module {
    override fun sendPulse(received: Pulse, from: String): Pulse? {
        memory[from] = received
        return if (memory.all { it.value == Pulse.HIGH }) {
            Pulse.LOW
        } else {
            Pulse.HIGH
        }
    }
}

fun countRx(moduleMap: Map<String, Module>): Long {
    val rxParents = moduleMap.filter { (_, v) ->
        "rx" in v.destinations
    }.map { (k, _) ->
        k
    }

    // Broadcaster and FlipFlop branch not tested, but theoretically work
    val fixedRxParents = rxParents.flatMap {
        when (val module = moduleMap[it] ?: error("invalid parent of rx: $it")) {
            is Broadcaster, is FlipFlop -> listOf(it to Pulse.LOW)
            is Conjunction -> module.memory.keys.map { s ->
                s to Pulse.HIGH
            }
        }
    }.toMap().toMutableMap()

    val found = mutableMapOf<String, Long>()
    var i = 0L

    while (fixedRxParents.isNotEmpty()) {
        val pulses = mutableListOf(mutableListOf<Pair<String, String>>(), mutableListOf())
        val curr = dequeOf(Triple("button", "broadcaster", Pulse.LOW))

        curr.drainFirst { (from, to, type) ->
            with(moduleMap[to] ?: return@drainFirst) {
                val pulse = this.sendPulse(type, from = from)
                pulse?.let { p ->
                    pulses[p.ordinal] += buildList {
                        this@with.destinations.forEach {
                            add(to to it)
                            this@drainFirst.addLast(Triple(to, it, p))
                        }
                    }
                }
            }
        }
        i++

        // needed to avoid concurrent modification of fixedRxParents
        val toRemove = buildList {
            fixedRxParents.forEach { (k, v) ->
                pulses[v.ordinal].filter { (from, _) ->
                    from == k
                }.forEach { (from, _) ->
                    add(from)
                    found[from] = i
                }
            }
        }

        // needed to avoid concurrent modification of fixedRxParents
        toRemove.forEach {
            fixedRxParents.remove(it)
        }
    }

    return found.values.reduce { acc, it ->
        acc / gcd(acc, it) * it
    }
}

fun main() {
    val conjunctions = mutableSetOf<String>()
    // unusual behavior, specifying generics on buildMap removes compilation error
    // if removed, forEach lambda infers incorrect return type (???)
    val moduleMap = buildMap<String, Module> {
        readInput("input/Day20").map {
            it.replace(" ", "").split("->")
        }.forEach {
            val (moduleName, destinationModules) = it
            if (moduleName == "broadcaster") {
                put(moduleName, Broadcaster(destinationModules.split(",")))
            } else if (moduleName.first() == '%') {
                put(moduleName.drop(1), FlipFlop(destinationModules.split(",")))
            } else if (moduleName.first() == '&') {
                with(moduleName.drop(1)) {
                    conjunctions.add(this)
                    put(this, Conjunction(destinationModules.split(",")))
                }
            } else {
                error("invalid module name $moduleName")
            }
        }
    }

    moduleMap.forEach { (k, v) ->
        with(v.destinations.intersect(conjunctions)) {
            if (isNotEmpty()) {
                forEach {
                    (moduleMap[it]!! as Conjunction).memory[k] = Pulse.LOW
                }
            }
        }
    }

    val part2 = moduleMap.map { it.key to when (val v = it.value) {
        is Broadcaster -> v.copy()
        is Conjunction -> v.copy()
        is FlipFlop -> v.copy()
    } }.toMap()

    val total = mutableListOf(0L, 0L)

    repeat(1000) {
        val curr = dequeOf(Triple("button", "broadcaster", Pulse.LOW))
        val counts = mutableListOf(0L, 0L)

        curr.drainFirst { (from, to, type) ->
            counts[type.ordinal]++
            with(moduleMap[to] ?: return@drainFirst) {
                val pulse = this.sendPulse(type, from = from)
                pulse?.let { p ->
                    this@with.destinations.forEach {
                        this@drainFirst.addLast(Triple(to, it, p))
                    }
                }
            }
        }

        total[0] += counts[0]
        total[1] += counts[1]
    }

    println(total.reduce(Long::times))

    countRx(part2).println()
}
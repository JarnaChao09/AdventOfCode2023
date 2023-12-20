import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/**
 * Conversion of Boolean to Int, true = 1, false = 0
 */
fun Boolean.toInt(): Int = if (this) 1 else 0

/**
 * Conversion of Boolean to Long, true = 1L, false = 0L
 */
fun Boolean.toLong(): Long = if (this) 1L else 0L

/**
 * Gives the transpose of a rectangular matrix (stored as a List<List<T>>)
 */
fun <T> List<List<T>>.transpose(): List<List<T>> = List(this[0].size) { r -> List(this.size) { c -> this[c][r] } }

/**
 * Enum for directions in (row, column) space
 */
enum class Direction(val r: Int, val c: Int) {
    Up(-1, 0),
    Right(0, +1),
    Down(+1, 0),
    Left(0, -1);

    operator fun component1(): Int = this.r

    operator fun component2(): Int = this.c
}

/**
 * Splits on empty collections to form a grouping of lists
 */
fun <T : Collection<*>> List<T>.splitOnEmpty(): List<List<T>> = buildList {
    var tmp = mutableListOf<T>()
    this@splitOnEmpty.forEach {
        if (it.isNotEmpty()) {
            tmp.add(it)
        } else {
            add(tmp.toList())
            tmp = mutableListOf()
        }
    }
    if (tmp.isNotEmpty()) {
        add(tmp.toList())
    }
}

/**
 * Common queue drain loop, provides access to the queue through implicit receiver
 *
 * NOTE:
 *  capturing the called variable in the block is also possible
 *  might change API to construct new array deque (i.e. (T) -> ArrayDeque<T>)
 */
fun <T> ArrayDeque<T>.drainFirst(block: ArrayDeque<T>.(T) -> Unit) {
    while (this.isNotEmpty()) {
        this.block(this.removeFirst())
    }
}

/**
 * Smart constructor for ArrayDeque<T>
 */
fun <T> dequeOf(vararg items: T): ArrayDeque<T> = ArrayDeque(items.toList())

/**
 * Function to calculate the greatest common denominator of two numbers
 */
fun gcd(a: Long, b: Long): Long = if (a == 0L) b else gcd(b % a, a)
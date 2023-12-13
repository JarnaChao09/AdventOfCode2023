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

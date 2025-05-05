package com.example.aniplayer.utils.parsers


import androidx.collection.ArrayMap
import androidx.collection.ArraySet
import java.util.*

fun Collection<*>?.sizeOrZero(): Int = this?.size ?: 0

fun <T> MutableCollection<T>.replaceWith(subject: Iterable<T>) {
    clear()
    addAll(subject)
}

fun <T, C : MutableCollection<in T>> Iterable<Iterable<T>>.flattenTo(destination: C): C {
    for (element in this) {
        destination.addAll(element)
    }
    return destination
}

fun <T> List<T>.medianOrNull(): T? = when {
    isEmpty() -> null
    else -> get((size / 2).coerceIn(indices))
}

inline fun <T, R> Collection<T>.mapToSet(transform: (T) -> R): Set<R> {
    return mapTo(ArraySet(size), transform)
}

inline fun <T, R : Any> Collection<T>.mapNotNullToSet(transform: (T) -> R?): Set<R> {
    val destination = ArraySet<R>(size)
    for (item in this) {
        destination.add(transform(item) ?: continue)
    }
    return destination
}

inline fun <T, reified R> Array<T>.mapToArray(transform: (T) -> R): Array<R> = Array(size) { i ->
    transform(get(i))
}

fun <K, V> List<Pair<K, V>>.toMutableMap(): MutableMap<K, V> = toMap(ArrayMap(size))

fun <T> MutableList<T>.move(sourceIndex: Int, targetIndex: Int) {
    if (sourceIndex <= targetIndex) {
        Collections.rotate(subList(sourceIndex, targetIndex + 1), -1)
    } else {
        Collections.rotate(subList(targetIndex, sourceIndex + 1), 1)
    }
}

fun <T> Iterator<T>.nextOrNull(): T? = if (hasNext()) next() else null

inline fun <T, K, V> Collection<T>.associateGrouping(transform: (T) -> Pair<K, V>): Map<K, Set<V>> {
    val result = LinkedHashMap<K, MutableSet<V>>(size)
    for (item in this) {
        val (k, v) = transform(item)
        result.getOrPut(k) { LinkedHashSet() }.add(v)
    }
    return result
}

fun <K> MutableMap<K, Int>.incrementAndGet(key: K): Int {
    var value = get(key) ?: 0
    value++
    put(key, value)
    return value
}

inline fun <T> MutableSet(size: Int, init: (index: Int) -> T): MutableSet<T> {
    val set = ArraySet<T>(size)
    repeat(size) { index -> set.add(init(index)) }
    return set
}

inline fun <T> Set(size: Int, init: (index: Int) -> T): Set<T> = when (size) {
    0 -> emptySet()
    1 -> Collections.singleton(init(0))
    else -> MutableSet(size, init)
}

@Suppress("UNCHECKED_CAST")
inline fun <T, reified R> Collection<T>.mapToArray(transform: (T) -> R): Array<R> {
    val result = arrayOfNulls<R>(size)
    forEachIndexed { index, t -> result[index] = transform(t) }
    return result as Array<R>
}

fun <T> Array<T>.toArraySet(): Set<T> = when (size) {
    0 -> emptySet()
    1 -> setOf(first())
    else -> ArraySet(this)
}

fun <T> Collection<T>.toArraySet(): Set<T> = when (size) {
    0 -> emptySet()
    1 -> setOf(first())
    else -> ArraySet(this)
}

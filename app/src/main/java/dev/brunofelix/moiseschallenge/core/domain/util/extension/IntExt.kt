package dev.brunofelix.moiseschallenge.core.domain.util.extension

/**
 * Converts a value to megabits (binary megabytes) in bytes.
 * Example: 100.toMegabits() = 100 * 1024 * 1024L
 */
fun Int.toMegabits(): Long = this.toLong() * 1024 * 1024

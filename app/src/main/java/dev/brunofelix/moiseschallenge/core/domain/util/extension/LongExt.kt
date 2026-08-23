package dev.brunofelix.moiseschallenge.core.domain.util.extension

import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Converts a duration in milliseconds into a formatted time string (e.g., "M:SS" or "MM:SS").
 *
 * @receiver The duration in milliseconds to be formatted.
 * @return A formatted time string representing the duration.
 */
fun Long.toFormattedTime(): String {
    if (this <= 0L) return "0:00"
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(minutes)
    return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
}
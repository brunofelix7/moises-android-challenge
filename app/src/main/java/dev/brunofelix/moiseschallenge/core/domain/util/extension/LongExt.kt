package dev.brunofelix.moiseschallenge.core.domain.util.extension

import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Converts a duration in milliseconds into a formatted time string (e.g., "M:SS" or "MM:SS").
 *
 * @receiver The duration in milliseconds to be formatted.
 * @param isRemaining If true, prepends a minus sign to the formatted string for positive durations.
 * @return A formatted time string representing the duration.
 */
fun Long.toFormattedTime(isRemaining: Boolean = false): String {
    if (this <= 0L) return "0:00"
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(minutes)
    val formatted = String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
    return if (isRemaining) "-$formatted" else formatted
}

/**
 * Ensures a safe range for the Slider, preventing crashes when the duration is zero or null.
 */
fun Long.toSafeSliderRange(): ClosedFloatingPointRange<Float> {
    val endValue = this.takeIf { it > 0 }?.toFloat() ?: 100f
    return 0f..endValue
}
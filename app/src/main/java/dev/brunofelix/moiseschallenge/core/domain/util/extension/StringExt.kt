package dev.brunofelix.moiseschallenge.core.domain.util.extension

/**
 * Converts iTunes image URL to a different size.
 *
 * @param size The size to convert to.
 * @return The URL with the converted size.
 */
fun String.toItunesImageSize(size: Int): String {
    return this.replace(Regex("\\d+x\\d+bb\\.jpg"), "${size}x${size}bb.jpg")
}
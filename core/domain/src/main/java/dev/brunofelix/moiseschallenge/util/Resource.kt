package dev.brunofelix.moiseschallenge.util

/**
 * A sealed interface representing a state of a resource that can be either [Success] or [Error].
 *
 * @param T The type of the data held by the resource.
 *
 * @see Success
 * @see Error
 */
sealed interface Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>
    data class Error(val throwable: Throwable) : Resource<Nothing>
}

/**
 * Returns the result of [onSuccess] for [Resource.Success] or [onFailure] for [Resource.Error].
 */
inline fun <T, R> Resource<T>.fold(
    onSuccess: (T) -> R,
    onFailure: (Throwable) -> R
): R {
    return when (this) {
        is Resource.Success -> onSuccess(data)
        is Resource.Error -> onFailure(throwable)
    }
}

/**
 * Extension function to convert a [Result] to a [Resource].
 *
 * @param T The type of the value in the [Result].
 * @return A [Resource.Success] if the [Result] is successful, or [Resource.Error] if it failed.
 */
fun <T> Result<T>.toResource(): Resource<T> {
    return fold(
        onSuccess = { Resource.Success(it) },
        onFailure = { Resource.Error(it) }
    )
}
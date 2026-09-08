package dev.brunofelix.moiseschallenge.util.extension

import retrofit2.HttpException
import retrofit2.Response

/**
 * Maps the body of a successful [Response] using the [transform] function, or throws
 * a mapped [RemoteException] if the response is unsuccessful or the body is null.
 *
 * @param transform Function to map the response body [T] to the result type [R].
 * @return The result of the [transform] function.
 * @throws RemoteException if the response is not successful.
 * @throws NullPointerException if the body is null on a successful response.
 */
inline fun <T, R> Response<T>.mapOrThrow(transform: (T) -> R): R {
    if (isSuccessful) {
        return body()?.let(transform) ?: throw NullPointerException("Response body is null")
    } else {
        throw HttpException(this).toRemoteException()
    }
}

/**
 * Maps the body of a successful [Response] to the result type [R], or throws
 *
 * @param transform Function to map the response body [T] to the result type [R].
 * @return The result of the [transform] function.
 */
fun Response<Unit>.mapOrThrow() {
    if (isSuccessful) return
    else throw HttpException(this).toRemoteException()
}

/**
 * Converts a [Response] to a [Result] type, mapping any errors to a [RemoteException].
 *
 * @return A [Result.success] with the body if successful, or a [Result.failure]
 * with a mapped [RemoteException] otherwise.
 */
fun <T> Response<T>.toResult(): Result<T> {
    return try {
        if (isSuccessful) {
            val body = body()
            if (body != null) {
                Result.success(body)
            } else {
                Result.failure(NullPointerException("Response body is null"))
            }
        } else {
            Result.failure(HttpException(this).toRemoteException())
        }
    } catch (e: Exception) {
        Result.failure(e.toRemoteException())
    }
}
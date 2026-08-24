package dev.brunofelix.moiseschallenge.core.data.util.extension

import dev.brunofelix.moiseschallenge.core.domain.util.exception.RemoteException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * Maps a [Throwable] to a [RemoteException] based on the exception type.
 *
 * This function handles:
 * - [IOException]: Maps to [RemoteException.NoInternet] or [RemoteException.Unknown].
 * - [HttpException]: Maps to specific [RemoteException] subclasses based on HTTP codes (401, 404, 5xx).
 * - [RemoteException]: Returns the exception as is.
 *
 * @return A [RemoteException] domain model.
 */
fun Throwable.toRemoteException(): RemoteException {
    return when (this) {
        is IOException -> {
            when (this) {
                is ConnectException, is UnknownHostException -> RemoteException.NoInternet()
                else -> RemoteException.Unknown()
            }
        }
        is HttpException -> {
            when (code()) {
                401 -> RemoteException.Unauthorized()
                404 -> RemoteException.NotFound()
                in 500..599 -> RemoteException.ServerError()
                else -> RemoteException.Unknown()
            }
        }
        is RemoteException -> this
        else -> RemoteException.Unknown()
    }
}

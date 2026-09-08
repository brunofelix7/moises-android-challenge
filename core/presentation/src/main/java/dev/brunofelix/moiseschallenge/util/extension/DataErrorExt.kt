package dev.brunofelix.moiseschallenge.util.extension

import dev.brunofelix.moiseschallenge.core.designsystem.R
import dev.brunofelix.moiseschallenge.util.exception.LocalException
import dev.brunofelix.moiseschallenge.util.exception.RemoteException
import dev.brunofelix.moiseschallenge.util.UiText

/**
 * Maps a [RemoteException] to a [UiText] for display in the presentation layer.
 *
 * @return A [UiText] representation of the network error.
 */
fun RemoteException.toUiText(): UiText {
    return when (this) {
        is RemoteException.Unauthorized -> UiText.StringResource(R.string.error_network_unauthorized)
        is RemoteException.NotFound -> UiText.StringResource(R.string.error_network_not_found)
        is RemoteException.ServerError -> UiText.StringResource(R.string.error_network_server)
        is RemoteException.NoInternet -> UiText.StringResource(R.string.error_network_no_internet)
        is RemoteException.Unknown -> UiText.StringResource(R.string.error_unknown)
        is RemoteException.ApiError -> UiText.DynamicString(this.message)
        is RemoteException.General -> UiText.StringResource(this.messageRes)
    }
}

/**
 * Maps a [LocalException] to a [UiText] for display in the presentation layer.
 *
 * @return A [UiText] representation of the local error.
 */
fun LocalException.toUiText(): UiText {
    return when (this) {
        is LocalException.DatabaseError -> UiText.StringResource(R.string.error_local_database)
        is LocalException.PermissionDenied -> UiText.StringResource(R.string.error_local_database)
        is LocalException.DiskFull -> UiText.StringResource(R.string.error_local_database)
        is LocalException.Unknown -> UiText.StringResource(R.string.error_unknown)
        is LocalException.General -> UiText.StringResource(this.messageRes)
    }
}

/**
 * Maps any [Throwable] to a [UiText].
 *
 * If the throwable is a [RemoteException] or [LocalException], it uses their respective
 * mapping functions. Otherwise, it returns a generic unknown error message.
 *
 * @return A [UiText] representation of the exception.
 */
fun Throwable.toUiText(): UiText {
    return when (this) {
        is RemoteException -> this.toUiText()
        is LocalException -> this.toUiText()
        else -> UiText.StringResource(R.string.error_unknown)
    }
}

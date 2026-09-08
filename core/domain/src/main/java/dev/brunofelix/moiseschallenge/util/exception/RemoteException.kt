package dev.brunofelix.moiseschallenge.util.exception

/**
 * Represents errors that occur during remote operations, such as network calls or API responses.
 *
 * @property errorCode The HTTP status code or a custom error code.
 * @property message An optional error message.
 * @property cause An optional cause of the exception.
 */
sealed class RemoteException(
    val errorCode: Int? = null,
    override val message: String? = null,
    override val cause: Throwable? = null
) : Exception(message, cause) {

    /**
     * Error when the user is unauthorized (HTTP 401).
     */
    class Unauthorized : RemoteException(errorCode = 401)

    /**
     * Error when the requested resource is not found (HTTP 404).
     */
    class NotFound : RemoteException(errorCode = 404)

    /**
     * Error when the server encounters an internal error (HTTP 500).
     */
    class ServerError : RemoteException(errorCode = 500)

    /**
     * Error when there is no internet connection.
     */
    class NoInternet : RemoteException()

    /**
     * An unknown remote error.
     */
    class Unknown : RemoteException()

    /**
     * A specific API error with a custom code and message.
     *
     * @property code The specific error code from the API.
     * @property message The error message from the API.
     */
    data class ApiError(
        val code: Int,
        override val message: String
    ) : RemoteException(errorCode = code, message = message)

    /**
     * A general remote error with a specific message resource.
     *
     * @property messageRes The string resource ID for the error message.
     * @property cause An optional cause of the error.
     */
    class General(
        val messageRes: Int,
        cause: Throwable? = null
    ) : RemoteException(cause = cause)
}

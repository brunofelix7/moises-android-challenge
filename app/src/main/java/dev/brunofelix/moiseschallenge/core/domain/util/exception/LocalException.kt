package dev.brunofelix.moiseschallenge.core.domain.util.exception

/**
 * Represents local errors that can occur during application execution,
 * such as database failures or permission issues.
 *
 * @property message An optional error message.
 * @property cause An optional cause of the exception.
 */
sealed class LocalException(
    override val message: String? = null,
    override val cause: Throwable? = null
) : Exception(message, cause) {

    /**
     * Error related to database operations.
     *
     * @property cause The original cause of the database error.
     */
    class DatabaseError(cause: Throwable? = null) : LocalException(cause = cause)

    /**
     * Error when a required permission is denied by the user or system.
     */
    class PermissionDenied : LocalException()

    /**
     * Error when the device storage is full.
     */
    class DiskFull : LocalException()

    /**
     * An unknown local error.
     */
    class Unknown : LocalException()

    /**
     * A general local error with a specific message resource.
     *
     * @property messageRes The string resource ID for the error message.
     * @property cause An optional cause of the error.
     */
    class General(val messageRes: Int, cause: Throwable? = null) : LocalException(cause = cause)
}

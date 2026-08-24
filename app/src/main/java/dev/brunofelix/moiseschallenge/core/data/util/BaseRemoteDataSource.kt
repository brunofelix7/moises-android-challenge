package dev.brunofelix.moiseschallenge.core.data.util

import dev.brunofelix.moiseschallenge.core.data.util.extension.mapOrThrow
import dev.brunofelix.moiseschallenge.core.data.util.extension.toRemoteException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import retrofit2.Response

/**
 * Base class for remote data sources using Retrofit.
 *
 * Provides utility methods to execute API calls safely,
 * handling exceptions and mapping responses to domain models.
 *
 * @param T The type of the Retrofit API interface.
 * @property api The API instance used for the calls.
 */
abstract class BaseRemoteDataSource<T : Any>(
    private val api: T
) {
    /**
     * Executes a suspended API call safely.
     *
     * @param R The domain model type returned in the [Result].
     * @param S The DTO (Data Transfer Object) type returned by the API.
     * @param call Block defining the API call to be executed.
     * @param transform Function that maps the DTO ([S]) to the domain model ([R]).
     * @return A [Result] containing the domain object on success, or an exception on failure.
     * @throws CancellationException if the coroutine is cancelled during execution.
     */
    protected suspend fun <R, S> safeApiCall(
        call: suspend T.() -> Response<S>,
        transform: (S) -> R
    ): Result<R> = withContext(Dispatchers.IO) {
        try {
            val response = api.call()
            Result.success(response.mapOrThrow(transform))
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(e.toRemoteException())
        }
    }

    /**
     * Executes a suspended API call that has no response body (Unit).
     *
     * @param call Block defining the API call to be executed.
     * @return A [Result] containing the HTTP status code on success, or the occurred exception on failure.
     * @throws CancellationException if the coroutine is cancelled.
     */
    protected suspend fun safeApiCall(
        call: suspend T.() -> Response<Unit>
    ): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val response = api.call()
            response.mapOrThrow()
            Result.success(response.code())
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            Result.failure(e.toRemoteException())
        }
    }

    /**
     * Executes an API call that returns a [Flow] of data.
     *
     * @param DTO The type of data returned by the API.
     * @param Model The mapped domain model type.
     * @param call Block defining the API call.
     * @param transform DTO to Model mapping function.
     * @return A [Flow] emitting the mapped data and executing on [Dispatchers.IO].
     */
    protected fun <DTO, Model> safeFlowApiCall(
        call: T.() -> Flow<DTO>,
        transform: (DTO) -> Model
    ): Flow<Model> {
        return api.call()
            .map { dto ->
                transform(dto)
            }
            .flowOn(Dispatchers.IO)
            .catch { error ->
                throw error.toRemoteException()
            }
    }
}

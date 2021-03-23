package skillmanagement.common.graphql

import graphql.ErrorType
import graphql.ErrorType.ValidationError
import graphql.ExceptionWhileDataFetching
import graphql.GraphQLError
import graphql.GraphqlErrorBuilder.newError
import graphql.kickstart.execution.error.GenericGraphQLError
import graphql.kickstart.execution.error.GraphQLErrorHandler
import mu.KotlinLogging
import org.springframework.core.NestedExceptionUtils.getRootCause
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Component
import skillmanagement.common.validation.ValidationException

@Component
internal class CustomGraphQLErrorHandler : GraphQLErrorHandler {

    private val log = KotlinLogging.logger {}
    private val generalServerError = GenericGraphQLError("Internal Server Error(s) while executing query")

    override fun processErrors(errors: List<GraphQLError>): List<GraphQLError> {
        val clientErrors = errors.filter(::isClientError)
        val serverErrors = errors - clientErrors

        clientErrors.forEach(::logClientError)
        serverErrors.forEach(::logServerError)

        val repackagedClientErrors = clientErrors.map(::repackageClientError)
        if (serverErrors.isNotEmpty()) {
            return repackagedClientErrors + generalServerError
        }
        return repackagedClientErrors
    }

    private fun isClientError(error: GraphQLError): Boolean {
        if (error is ExceptionWhileDataFetching) {
            return when (val exception = error.exception) {
                is AccessDeniedException -> true
                is ValidationException -> true
                is IllegalArgumentException -> true
                else -> exception is GraphQLError
            }
        }
        return true
    }

    private fun repackageClientError(clientError: GraphQLError): GraphQLError {
        if (clientError is ExceptionWhileDataFetching) {
            val exception = clientError.exception
            return when (val rootCause = getRootCause(exception)) {
                is ValidationException -> clientError.repackage(rootCause.message, ValidationError)
                is IllegalArgumentException -> clientError.repackage(rootCause.message, ValidationError)
                else -> clientError.repackage(exception.message)
            }
        }
        return clientError
    }

    private fun ExceptionWhileDataFetching.repackage(message: String?, errorType: ErrorType? = null) = newError()
        .message(message)
        .errorType(errorType ?: this.errorType)
        .locations(this.locations)
        .path(this.path)
        .build()

    private fun logClientError(error: GraphQLError) {
        when (error) {
            is Throwable -> logClientException(error.message, error)
            is ExceptionWhileDataFetching -> logClientException(error.message, error.exception)
            else -> logClientException(error.message)
        }
    }

    private fun logClientException(message: String?, exception: Throwable? = null) {
        log.warn(exception) { "Client-Side Error: $message" }
    }

    private fun logServerError(error: GraphQLError) {
        when (error) {
            is Throwable -> logServerException(error.message, error)
            is ExceptionWhileDataFetching -> logServerException(error.message, error.exception)
            else -> logServerException(error.message)
        }
    }

    private fun logServerException(message: String?, exception: Throwable? = null) {
        log.error(exception) { "Server-Side Error: $message" }
    }

}

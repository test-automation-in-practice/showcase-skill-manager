package skillmanagement.common.graphql

import graphql.ErrorClassification
import graphql.ErrorType.ValidationError

class GraphQLClientSideException(cause: Throwable) : GraphQLRuntimeException(cause.message, cause) {
    override fun getErrorType(): ErrorClassification = ValidationError
}

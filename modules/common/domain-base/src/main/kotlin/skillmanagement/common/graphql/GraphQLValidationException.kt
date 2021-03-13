package skillmanagement.common.graphql

import graphql.ErrorClassification
import graphql.ErrorType.ValidationError
import skillmanagement.common.validation.ValidationException

class GraphQLValidationException(cause: ValidationException) : GraphQLRuntimeException(cause.message, cause) {
    override fun getErrorType(): ErrorClassification = ValidationError
}

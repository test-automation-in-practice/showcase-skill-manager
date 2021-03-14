package skillmanagement.common.graphql

import skillmanagement.common.validation.ValidationException

fun <T> withErrorHandling(block: () -> T): T {
    try {
        return block()
    } catch (e: ValidationException) {
        throw GraphQLClientSideException(e)
    } catch (e: IllegalArgumentException) {
        throw GraphQLClientSideException(e)
    }
}

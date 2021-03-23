package skillmanagement.common.graphql

import graphql.ExceptionWhileDataFetching
import graphql.execution.DataFetcherExceptionHandler
import graphql.execution.DataFetcherExceptionHandlerParameters
import graphql.execution.DataFetcherExceptionHandlerResult
import org.springframework.stereotype.Component

@Component
internal class CustomDataFetcherExceptionHandler : DataFetcherExceptionHandler {

    override fun onException(
        handlerParameters: DataFetcherExceptionHandlerParameters
    ): DataFetcherExceptionHandlerResult {
        val error = handlerParameters.toError()
        return DataFetcherExceptionHandlerResult.newResult().error(error).build()
    }

    private fun DataFetcherExceptionHandlerParameters.toError() =
        ExceptionWhileDataFetching(path, exception, sourceLocation)

}

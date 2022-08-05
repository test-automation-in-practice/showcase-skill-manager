package skillmanagement.common.graphql

import graphql.language.IntValue
import graphql.schema.Coercing
import graphql.schema.CoercingParseLiteralException
import graphql.schema.CoercingParseValueException
import graphql.schema.CoercingSerializeException
import skillmanagement.common.model.IntType

abstract class IntTypeCoercing<T : IntType> : Coercing<T, Int> {

    protected abstract val typeName: String

    override fun serialize(dataFetcherResult: Any): Int {
        if (dataFetcherResult is IntType) return dataFetcherResult.toInt()
        throw CoercingSerializeException("Unable to serialize [$dataFetcherResult] as an Int")
    }

    override fun parseValue(input: Any): T {
        if (input is Number) return createInstance(input.toInt())
        throw CoercingParseValueException("Value is not a $typeName: $input")
    }

    override fun parseLiteral(input: Any): T {
        if (input is IntValue) return createInstance(input.value.toInt())
        throw CoercingParseLiteralException("Value is not a $typeName: $input")
    }

    protected abstract fun createInstance(value: Int): T

}

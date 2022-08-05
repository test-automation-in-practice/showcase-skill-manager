package skillmanagement.common.graphql

import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType

fun scalarType(name: String, coercing: Coercing<*, *>): GraphQLScalarType =
    GraphQLScalarType.newScalar().name(name).coercing(coercing).build()

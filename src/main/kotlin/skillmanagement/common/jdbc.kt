package skillmanagement.common

import mu.KotlinLogging
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.UUID

private val log = KotlinLogging.logger("jdbc")

fun NamedParameterJdbcTemplate.insert(table: String, columnValues: List<Pair<String, Any?>>) {
    require(columnValues.isNotEmpty()) { "columnValues must not be empty!" }

    val columns = columnValues.map { it.first }.joinToString()
    val variables = columnValues.map { ":${it.first}" }.joinToString()

    val statement = "INSERT INTO $table ($columns) VALUES ($variables)"
    val parameters = columnValues.toMap()

    log.info { "Executing [$statement] with parameters [$parameters]" }
    this.update(statement, parameters)
}

fun NamedParameterJdbcTemplate.update(
    table: String,
    columnValues: List<Pair<String, Any?>>,
    whereConditions: List<Pair<String, Any?>>
) {
    require(columnValues.isNotEmpty()) { "columnValues must not be empty!" }
    require(whereConditions.isNotEmpty()) { "whereConditions must not be empty!" }

    val values = columnValues.map { "${it.first} = :${it.first}" }.joinToString()
    val where = whereConditions.map { "${it.first} = :${it.first}" }.joinToString(separator = " AND ")

    val statement = "UPDATE $table SET $values WHERE $where"
    val parameters = columnValues.toMap() + whereConditions.toMap()

    log.info { "Executing [$statement] with parameters [$parameters]" }
    this.update(statement, parameters)
}

fun NamedParameterJdbcTemplate.deleteById(table: String, id: UUID) {
    val statement = "DELETE FROM $table WHERE id = :id"
    val parameters = mapOf("id" to id.toString())

    log.info { "Executing [$statement] with parameters [$parameters]" }
    this.update(statement, parameters)
}

fun NamedParameterJdbcTemplate.delete(table: String, whereConditions: List<Pair<String, Any?>>) {
    require(whereConditions.isNotEmpty()) { "whereConditions must not be empty!" }

    val whereClause = whereConditions.map { "${it.first} = :${it.first}" }.joinToString(separator = " AND ")

    val statement = "DELETE FROM $table WHERE $whereClause"
    val parameters = whereConditions.toMap()

    log.info { "Executing [$statement] with parameters [$parameters]" }
    this.update(statement, parameters)
}

package skillmanagement.common

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

fun NamedParameterJdbcTemplate.insert(tableName: String, columnValueMapping: List<Pair<String, Any?>>) {
    val columns = columnValueMapping.map { it.first }.joinToString()
    val variables = columnValueMapping.map { ":${it.first}" }.joinToString()
    val parameters = columnValueMapping.toMap()

    val statement = "INSERT INTO $tableName ($columns) VALUES ($variables)"
    this.update(statement, parameters)
}

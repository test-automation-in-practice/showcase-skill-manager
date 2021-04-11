package skillmanagement.domain.employees.searchindex

import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.index.query.Operator.AND
import org.elasticsearch.index.query.QueryBuilders.queryStringQuery
import org.elasticsearch.index.query.QueryStringQueryBuilder
import org.elasticsearch.search.SearchHit
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import skillmanagement.common.searchindices.AbstractSearchIndex
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.model.EmployeeId
import skillmanagement.domain.employees.model.employeeId

@Component
internal class EmployeeSearchIndex(
    override val client: RestHighLevelClient
) : AbstractSearchIndex<Employee, EmployeeId>() {

    override val indexName = "employees"
    override val sortFieldName = "_sort"
    override val suggestFieldName = "name"

    override val mappingResource = ClassPathResource("/indices/employees-mapping.json")

    override fun toSource(instance: Employee) =
        with(instance) {
            mapOf(
                "name" to compositeName(),
                "firstName" to firstName.toString(),
                "lastName" to lastName.toString(),
                "title" to title.toString(),
                "email" to email.toString(),
                "skills" to skills.map { it.skill.label },
                "projects" to projects.map { it.project.label },
                "_sort" to "$lastName, $firstName",
                "_skillIds" to skills.map { it.skill.id.toString() },
                "_projectIds" to projects.map { it.project.id.toString() }
            )
        }

    override fun id(hit: SearchHit) = employeeId(hit.id)

    override fun buildQuery(queryString: String): QueryStringQueryBuilder =
        queryStringQuery(queryString)
            .defaultField("name")
            .defaultOperator(AND)

}

package skillmanagement.domain.projects.searchindex

import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.index.query.Operator.AND
import org.elasticsearch.index.query.QueryBuilders.queryStringQuery
import org.elasticsearch.index.query.QueryStringQueryBuilder
import org.elasticsearch.search.SearchHit
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import skillmanagement.common.searchindices.AbstractSearchIndex
import skillmanagement.domain.projects.model.Project
import skillmanagement.domain.projects.model.ProjectId
import skillmanagement.domain.projects.model.projectId

@Component
internal class ProjectSearchIndex(
    override val client: RestHighLevelClient
) : AbstractSearchIndex<Project, ProjectId>() {

    override val indexName = "projects"
    override val sortFieldName = "_sort"
    override val suggestFieldName = "label"

    override val mappingResource = ClassPathResource("/indices/projects-mapping.json")

    override fun toSource(instance: Project) =
        mapOf(
            "label" to instance.label.toString(),
            "description" to instance.description.toString(),
            "_sort" to instance.label.toString()
        )

    override fun id(hit: SearchHit) = projectId(hit.id)

    override fun buildQuery(queryString: String): QueryStringQueryBuilder =
        queryStringQuery(queryString)
            .defaultField("label")
            .defaultOperator(AND)


}

package skillmanagement.domain.skills.searchindex

import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.index.query.Operator.AND
import org.elasticsearch.index.query.QueryBuilders.queryStringQuery
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import skillmanagement.common.search.AbstractSearchIndex
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.model.Tag

@Component
class SkillSearchIndex(
    override val client: RestHighLevelClient
) : AbstractSearchIndex<Skill>() {

    override val indexName = "skills"
    override val mappingResource = ClassPathResource("/searchindex/skills-mapping.json")

    override fun toSource(instance: Skill) =
        mapOf(
            "label" to instance.label.toString(),
            "tags" to instance.tags.map(Tag::toString)
        )

    override fun id(instance: Skill) = instance.id

    override fun buildQuery(queryString: String) =
        queryStringQuery(queryString)
            .defaultField("label")
            .defaultOperator(AND)

}
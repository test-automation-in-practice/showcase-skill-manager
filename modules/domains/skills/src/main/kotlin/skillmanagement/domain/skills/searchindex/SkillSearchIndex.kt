@file:Suppress("DEPRECATION") // TODO migrate from RestHighLevelClient to ElasticsearchClient

package skillmanagement.domain.skills.searchindex

import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.index.query.Operator.AND
import org.elasticsearch.index.query.QueryBuilders.queryStringQuery
import org.elasticsearch.index.query.QueryStringQueryBuilder
import org.elasticsearch.search.SearchHit
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import skillmanagement.common.searchindices.AbstractSearchIndex
import skillmanagement.domain.skills.model.SkillEntity
import skillmanagement.domain.skills.model.SkillId
import skillmanagement.domain.skills.model.Tag
import skillmanagement.domain.skills.model.skillId

@Component
internal class SkillSearchIndex(
    override val client: RestHighLevelClient
) : AbstractSearchIndex<SkillEntity, SkillId>() {

    override val indexName = "skills"
    override val sortFieldName = "_sort"
    override val suggestFieldName = "label"

    override val mappingResource = ClassPathResource("/indices/skills-mapping.json")

    override fun toSource(instance: SkillEntity) =
        with(instance.data) {
            mutableMapOf(
                "label" to label.toString(),
                "tags" to tags.map(Tag::toString),
                "_sort" to label.toString()
            ).apply {
                if (description != null) {
                    put("description", description.toString())
                }
            }
        }

    override fun id(hit: SearchHit) = skillId(hit.id)

    override fun buildQuery(queryString: String): QueryStringQueryBuilder =
        queryStringQuery(queryString)
            .defaultField("label")
            .defaultOperator(AND)

}

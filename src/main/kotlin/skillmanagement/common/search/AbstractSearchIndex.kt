package skillmanagement.common.search

import org.elasticsearch.action.DocWriteRequest.OpType.INDEX
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest
import org.elasticsearch.action.delete.DeleteRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.RequestOptions.DEFAULT
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.indices.CreateIndexRequest
import org.elasticsearch.client.indices.GetIndexRequest
import org.elasticsearch.common.unit.TimeValue.timeValueMinutes
import org.elasticsearch.common.xcontent.XContentType.JSON
import org.elasticsearch.index.query.QueryStringQueryBuilder
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.sort.ScoreSortBuilder
import org.elasticsearch.search.sort.SortOrder.DESC
import org.springframework.core.io.Resource
import skillmanagement.common.resources.readAsString
import java.util.UUID
import javax.annotation.PostConstruct

abstract class AbstractSearchIndex<T : Any> {

    protected abstract val client: RestHighLevelClient

    protected abstract val indexName: String
    protected abstract val mappingResource: Resource

    @PostConstruct
    fun init() {
        if (!indexExists()) createIndex()
    }

    fun index(instance: T) {
        val request = IndexRequest(indexName)
            .id(id(instance).toString())
            .source(toSource(instance), JSON)
            .opType(INDEX)
        client.index(request, DEFAULT)
    }

    protected abstract fun toSource(instance: T): Map<String, Any>
    protected abstract fun id(instance: T): UUID

    fun deleteById(id: UUID) {
        client.delete(DeleteRequest(indexName, id.toString()), DEFAULT)
    }

    fun query(queryString: String): List<UUID> {
        val source = SearchSourceBuilder()
            .fetchSource(false)
            .query(buildQuery(queryString))
            .from(0)
            .size(10_000)
            .sort(ScoreSortBuilder().order(DESC))
        val request = SearchRequest(indexName)
            .source(source)

        val response = client.search(request, DEFAULT)
        val hits = response.hits.hits
        return hits.map { UUID.fromString(it.id) }
    }

    protected abstract fun buildQuery(queryString: String): QueryStringQueryBuilder

    fun reset() {
        deleteIndex()
        createIndex()
    }

    private fun indexExists(): Boolean =
        client.indices().exists(GetIndexRequest(indexName), DEFAULT)

    private fun createIndex() {
        val request = CreateIndexRequest(indexName)
            .mapping(mappingResource.readAsString(), JSON)
        client.indices().create(request, DEFAULT)
    }

    private fun deleteIndex() {
        val request = DeleteIndexRequest(indexName)
            .timeout(timeValueMinutes(1))
        client.indices().delete(request, DEFAULT)
    }

}

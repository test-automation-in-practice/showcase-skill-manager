package skillmanagement.common.searchindices

import org.elasticsearch.action.DocWriteRequest.OpType.INDEX
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest
import org.elasticsearch.action.delete.DeleteRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy.IMMEDIATE
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy.NONE
import org.elasticsearch.client.RequestOptions.DEFAULT
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.indices.CreateIndexRequest
import org.elasticsearch.client.indices.GetIndexRequest
import org.elasticsearch.common.unit.TimeValue.timeValueMinutes
import org.elasticsearch.common.xcontent.XContentType.JSON
import org.elasticsearch.index.query.MatchAllQueryBuilder
import org.elasticsearch.index.query.Operator.AND
import org.elasticsearch.index.query.QueryBuilder
import org.elasticsearch.index.query.QueryBuilders.queryStringQuery
import org.elasticsearch.index.query.QueryStringQueryBuilder
import org.elasticsearch.search.SearchHit
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.sort.FieldSortBuilder
import org.elasticsearch.search.sort.ScoreSortBuilder
import org.elasticsearch.search.sort.SortBuilder
import org.elasticsearch.search.sort.SortOrder.DESC
import org.springframework.beans.factory.InitializingBean
import org.springframework.core.io.Resource
import skillmanagement.common.model.IdType
import skillmanagement.common.model.Page
import skillmanagement.common.model.Pagination
import skillmanagement.common.model.Suggestion
import java.io.BufferedReader

abstract class AbstractSearchIndex<T : Any, ID : IdType> : SearchIndex<T, ID>, SearchIndexAdmin<T>, InitializingBean {

    protected abstract val client: RestHighLevelClient

    protected abstract val indexName: String
    protected abstract val sortFieldName: String
    protected abstract val suggestFieldName: String
    protected abstract val mappingResource: Resource

    private var refreshPolicy: RefreshPolicy = NONE

    fun enabledTestMode() {
        refreshPolicy = IMMEDIATE
    }

    override fun afterPropertiesSet() {
        if (!indexExists()) createIndex()
    }

    override fun index(instance: T) {
        val request = IndexRequest(indexName)
            .setRefreshPolicy(refreshPolicy)
            .id(id(instance).toString())
            .source(toSource(instance), JSON)
            .opType(INDEX)
        client.index(request, DEFAULT)
    }

    protected abstract fun toSource(instance: T): Map<String, Any>
    protected abstract fun id(instance: T): IdType

    override fun deleteById(id: ID) {
        val request = DeleteRequest(indexName, id.toString())
            .setRefreshPolicy(refreshPolicy)
        client.delete(request, DEFAULT)
    }

    override fun query(query: PagedStringQuery): Page<ID> =
        queryForIds(
            query = buildQuery(query.queryString),
            pagination = query.pagination,
            sort = ScoreSortBuilder().order(DESC)
        )

    override fun findAll(query: PagedFindAllQuery): Page<ID> =
        queryForIds(
            query = MatchAllQueryBuilder(),
            pagination = query.pagination,
            sort = FieldSortBuilder(sortFieldName)
        )

    private fun queryForIds(query: QueryBuilder, pagination: Pagination, sort: SortBuilder<*>): Page<ID> {
        val source = SearchSourceBuilder()
            .fetchSource(false)
            .query(query)
            .from(pagination.index.toInt() * pagination.size.toInt())
            .size(pagination.size.toInt())
            .sort(sort)
        val request = SearchRequest(indexName)
            .source(source)

        val response = client.search(request, DEFAULT)

        val searchHits = response.hits
        return Page(
            content = searchHits.hits.map { id(it) },
            pageIndex = pagination.index.toInt(),
            pageSize = pagination.size.toInt(),
            totalElements = searchHits.totalHits?.value ?: 0
        )
    }

    override fun suggest(input: String, max: MaxSuggestions): List<Suggestion> {
        val query = queryStringQuery("*$input*")
            .field(suggestFieldName)
            .defaultOperator(AND)
        val source = SearchSourceBuilder()
            .fetchSource(suggestFieldName, null)
            .query(query)
            .from(0)
            .size(max.toInt())
            .sort(ScoreSortBuilder().order(DESC))
        val request = SearchRequest(indexName)
            .source(source)

        val response = client.search(request, DEFAULT)
        return response.hits.map(::suggestion)
    }

    private fun suggestion(hit: SearchHit): Suggestion =
        Suggestion(
            id = id(hit),
            label = (hit.sourceAsMap[suggestFieldName] as? String) ?: ""
        )

    protected abstract fun id(hit: SearchHit): ID
    protected abstract fun buildQuery(queryString: String): QueryStringQueryBuilder

    override fun reset() {
        if (indexExists()) {
            deleteIndex()
        }
        createIndex()
    }

    override fun refresh() {
        client.indices().refresh(RefreshRequest(indexName), DEFAULT)
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

    override fun toString() = "SearchIndex(indexName='$indexName')"

    private fun Resource.readAsString(): String =
        inputStream.bufferedReader().use(BufferedReader::readText)

}

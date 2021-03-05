package skillmanagement.common.searchindices

import org.apache.http.HttpHost
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("storage.elasticsearch")
data class ElasticsearchProperties(
    val schema: String = "http",
    val host: String = "localhost",
    val port: Int = 9200
) {
    fun toHttpHost() = HttpHost(host, port, schema)
}

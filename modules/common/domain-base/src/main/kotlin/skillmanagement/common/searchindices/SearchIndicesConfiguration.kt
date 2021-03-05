package skillmanagement.common.searchindices

import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * Provides components needed to use Elasticsearch.
 *
 * @see RestHighLevelClient
 */
@Configuration
@ComponentScan
@EnableConfigurationProperties(ElasticsearchProperties::class)
class SearchIndicesConfiguration {

    @Bean(destroyMethod = "close")
    fun elasticsearchClient(properties: ElasticsearchProperties): RestHighLevelClient =
        createElasticsearchClient(properties)

}

fun createElasticsearchClient(properties: ElasticsearchProperties): RestHighLevelClient =
    RestHighLevelClient(RestClient.builder(properties.toHttpHost()))

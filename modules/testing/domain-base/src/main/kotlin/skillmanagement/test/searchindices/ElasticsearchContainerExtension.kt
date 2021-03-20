package skillmanagement.test.searchindices

import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.springframework.core.io.ClassPathResource
import skillmanagement.test.docker.AbstractDockerContainerExtension
import skillmanagement.test.docker.Container
import kotlin.annotation.AnnotationTarget.CLASS

@Retention
@Target(CLASS)
@ExtendWith(ElasticsearchContainerExtension::class)
annotation class RunWithDockerizedElasticsearch

class ElasticsearchContainer : Container("elasticsearch:7.6.2") {
    override fun getMappedPort(): Int = getMappedPort(9200)
}

private class ElasticsearchContainerExtension : AbstractDockerContainerExtension<ElasticsearchContainer>() {

    override val port = 9200
    override val portProperty = "ELASTICSEARCH_PORT"
    override fun createResource() = ElasticsearchContainer()
        .apply { addEnvFromFile(ClassPathResource("elasticsearch.env")) }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean =
        isElasticsearchContainer(parameterContext) || isElasticsearchClient(parameterContext)

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any? =
        when {
            isElasticsearchContainer(parameterContext) -> getOrInitializeContainerResource(extensionContext)
            isElasticsearchClient(parameterContext) -> getOrInitializeClient(extensionContext)
            else -> null
        }

    private fun getOrInitializeClient(context: ExtensionContext) =
        context.client ?: initClient(context).also { context.client = it }

    private fun initClient(context: ExtensionContext): RestHighLevelClient {
        val containerResource = getOrInitializeContainerResource(context)
        val port = containerResource.getContainer().getMappedPort()
        val httpHost = HttpHost("localhost", port, "http")
        return RestHighLevelClient(RestClient.builder(httpHost))
    }

    @Suppress("UNCHECKED_CAST")
    private var ExtensionContext.client: RestHighLevelClient?
        get() = getStore(namespace).get("client") as? RestHighLevelClient
        set(value) = getStore(namespace).put("client", value)

    private fun isElasticsearchContainer(parameterContext: ParameterContext) =
        parameterContext.parameter.type == ElasticsearchContainer::class.java

    private fun isElasticsearchClient(parameterContext: ParameterContext) =
        parameterContext.parameter.type == RestHighLevelClient::class.java

}

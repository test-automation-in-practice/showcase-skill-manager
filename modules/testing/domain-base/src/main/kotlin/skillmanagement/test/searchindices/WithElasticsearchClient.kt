@file:Suppress("DEPRECATION") // TODO migrate from RestHighLevelClient to ElasticsearchClient

package skillmanagement.test.searchindices

import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ParameterContext
import org.junit.jupiter.api.extension.ParameterResolver
import kotlin.annotation.AnnotationTarget.CLASS

@Retention
@Target(CLASS)
@ExtendWith(WithElasticsearchClientExtension::class)
annotation class WithElasticsearchClient

private class WithElasticsearchClientExtension : ParameterResolver {

    private val namespace: ExtensionContext.Namespace = ExtensionContext.Namespace.create(javaClass)

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean =
        parameterContext.parameter.type == RestHighLevelClient::class.java

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any =
        extensionContext.client

    private val ExtensionContext.client: RestHighLevelClient
        get() = getStore(namespace).getOrComputeIfAbsent("client", { initClient() }, RestHighLevelClient::class.java)

    private fun initClient(): RestHighLevelClient {
        val port = System.getProperty(ELASTICSEARCH_DOCKER_PORT_PROPERTY)?.toInt()
            ?: error("no $ELASTICSEARCH_DOCKER_PORT_PROPERTY system property value")
        val httpHost = HttpHost("localhost", port, "http")
        return RestHighLevelClient(RestClient.builder(httpHost))
    }

}

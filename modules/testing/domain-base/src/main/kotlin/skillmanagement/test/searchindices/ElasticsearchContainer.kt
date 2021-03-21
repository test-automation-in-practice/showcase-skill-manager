package skillmanagement.test.searchindices

import org.springframework.core.io.ClassPathResource
import skillmanagement.test.docker.Container
import skillmanagement.test.docker.DockerContainerFactory

const val ELASTICSEARCH_DOCKER_PORT_PROPERTY = "ELASTICSEARCH_PORT"

class ElasticsearchContainer : Container("elasticsearch:7.6.2") {
    override val port = 9200
    override val portProperty = ELASTICSEARCH_DOCKER_PORT_PROPERTY
}

class ElasticsearchContainerFactory : DockerContainerFactory<ElasticsearchContainer> {
    override fun createContainer() = ElasticsearchContainer()
        .apply { addEnvFromFile(ClassPathResource("elasticsearch.env")) }
}

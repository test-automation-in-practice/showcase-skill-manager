package skillmanagement.test.searchindices

import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.docker.WithDockerContainers
import kotlin.annotation.AnnotationTarget.CLASS

@Retention
@Target(CLASS)
@TechnologyIntegrationTest
@WithDockerContainers(ElasticsearchContainerFactory::class)
@WithElasticsearchClient
annotation class SearchIndexIntegrationTest

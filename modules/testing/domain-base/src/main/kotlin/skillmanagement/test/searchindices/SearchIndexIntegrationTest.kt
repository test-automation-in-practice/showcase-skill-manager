package skillmanagement.test.searchindices

import skillmanagement.test.TechnologyIntegrationTest
import kotlin.annotation.AnnotationTarget.CLASS

@Retention
@Target(CLASS)
@TechnologyIntegrationTest
@RunWithDockerizedElasticsearch
annotation class SearchIndexIntegrationTest

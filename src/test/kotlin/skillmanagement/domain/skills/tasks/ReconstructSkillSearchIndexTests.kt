package skillmanagement.domain.skills.tasks

import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.docker.ElasticsearchContainer
import skillmanagement.test.docker.RunWithDockerizedElasticsearch

@TechnologyIntegrationTest
@RunWithDockerizedElasticsearch
internal class ReconstructSkillSearchIndexTests(
    container: ElasticsearchContainer
)

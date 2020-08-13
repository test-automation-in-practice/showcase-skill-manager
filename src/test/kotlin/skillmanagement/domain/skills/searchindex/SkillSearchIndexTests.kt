package skillmanagement.domain.skills.searchindex

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import skillmanagement.configuration.ElasticsearchProperties
import skillmanagement.configuration.createElasticsearchClient
import skillmanagement.domain.skills.model.Skill
import skillmanagement.domain.skills.model.skill
import skillmanagement.test.TechnologyIntegrationTest
import skillmanagement.test.docker.ElasticsearchContainer
import skillmanagement.test.docker.RunWithDockerizedElasticsearch
import java.util.UUID

@TechnologyIntegrationTest
@RunWithDockerizedElasticsearch
internal class SkillSearchIndexTests(
    container: ElasticsearchContainer
) {

    val properties = ElasticsearchProperties(port = container.getMappedPort())
    val cut = SkillSearchIndex(createElasticsearchClient(properties))

    val kotlin1 = skill(label = "Kotlin #1", tags = setOf("language", "cool"))
    val kotlin2 = skill(label = "Kotlin #2")
    val python = skill(label = "Python", tags = setOf("language", "scripting"))
    val java = skill(label = "Java", tags = setOf("language"))

    @BeforeEach
    fun reset() {
        cut.reset()
    }

    @Test
    fun `querying an empty index does not return any IDs`() {
        assertThat(cut.query("foo")).isEmpty()
    }

    @Test
    fun `query can be used to search skill label and tags`() {
        index(kotlin1, python, java, kotlin2)

        assertThat(cut.query("kotlin")).containsOnly(kotlin1.id, kotlin2.id)
        assertThat(cut.query("label:*o*")).containsOnly(kotlin1.id, kotlin2.id, python.id)
        assertThat(cut.query("tags:language")).containsOnly(kotlin1.id, python.id, java.id)
        assertThat(cut.query("tags:cool")).containsOnly(kotlin1.id)
        assertThat(cut.query("label:kotlin AND tags:language")).containsOnly(kotlin1.id)
    }

    @Test
    fun `query only uses label by default`() {
        index(kotlin1, python, java, kotlin2)

        assertThat(cut.query("python")).containsOnly(python.id)
        assertThat(cut.query("kotlin")).containsOnly(kotlin1.id, kotlin2.id)
        assertThat(cut.query("language")).isEmpty()
    }

    @Test
    fun `entries can be deleted`() {
        index(kotlin1, kotlin2)
        assertThat(cut.query("kotlin")).containsOnly(kotlin1.id, kotlin2.id)
        delete(kotlin1.id)
        assertThat(cut.query("kotlin")).containsOnly(kotlin2.id)
    }

    private fun index(vararg skills: Skill) {
        skills.forEach(cut::index)
        cut.refresh()
    }

    private fun delete(vararg ids: UUID) {
        ids.forEach(cut::deleteById)
        cut.refresh()
    }
}

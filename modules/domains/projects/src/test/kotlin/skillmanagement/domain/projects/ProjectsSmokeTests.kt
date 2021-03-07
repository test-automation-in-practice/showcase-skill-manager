package skillmanagement.domain.projects

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.server.LocalServerPort
import skillmanagement.common.searchindices.SearchIndexAdmin
import skillmanagement.domain.projects.model.Project
import skillmanagement.test.SmokeTest
import skillmanagement.test.e2e.SpringBootTestWithDockerizedDependencies
import java.lang.Thread.sleep

private const val CREATE = 1
private const val READ = 10
private const val DELETE = 20
private const val ACTUATOR = 100

@SmokeTest
@SpringBootTestWithDockerizedDependencies
@TestMethodOrder(OrderAnnotation::class)
internal class ProjectsSmokeTests(
    @Autowired val searchIndex: SearchIndexAdmin<Project>,
    @LocalServerPort val port: Int
) {

    val projects = ProjectsTestDriver(port = port)

    @Order(CREATE)
    @TestFactory
    fun `projects can be created`(): List<DynamicTest> =
        listOf(
            dynamicTest("create 'Orbis'") { projects.create(label = "Orbis") },
            dynamicTest("create 'Neo'") { projects.create(label = "Neo") },
            dynamicTest("create 'Morpheus'") { projects.create(label = "Morpheus") },
            dynamicTest("create 'Scorpio'") { projects.create(label = "Scorpio") },
            dynamicTest("create 'NX'") { projects.create(label = "NX") },
            dynamicTest("[wait for index refresh]") { waitUntilSearchIndexIsRefreshed() }
        )

    @Order(READ)
    @TestFactory
    fun `projects can be got by their ID`(): List<DynamicTest> =
        projects.getAll()
            .also { assertThat(it).isNotEmpty }
            .map { project ->
                dynamicTest("get '${project.label}'") {
                    assertThat(projects.get(project.id)).isEqualTo(project)
                }
            }

    @Order(READ)
    @TestFactory
    fun `getting all projects returns a paged result`() =
        listOf(
            0 to listOf("Morpheus", "NX", "Neo"),
            1 to listOf("Orbis", "Scorpio")
        ).map { (page, expected) ->
            dynamicTest("getAll page #$page >> $expected") {
                assertThat(getAllLabels(page = page)).isEqualTo(expected)
            }
        }

    private fun getAllLabels(page: Int): List<String> =
        projects.getAll(page = page, size = 3).map { it.label.toString() }

    @Order(READ)
    @TestFactory
    fun `searching for projects returns a paged result`() =
        listOf(
            "*orp*" to setOf("Morpheus", "Scorpio"),
            "neo" to setOf("Neo")
        ).map { (query, expected) ->
            dynamicTest("query [$query] >> $expected") {
                assertThat(searchLabels(query)).isEqualTo(expected)
            }
        }

    private fun searchLabels(query: String): Set<String> =
        projects.search(query = query, page = 0, size = 100).map { it.label.toString() }.toSet()

    @Order(READ)
    @TestFactory
    fun `getting suggestions for projects returns their labels`() =
        listOf(
            "or" to setOf("Morpheus", "Orbis", "Scorpio"),
            "X" to setOf("NX")
        ).map { (input, expected) ->
            dynamicTest("suggest for [$input] >> $expected") {
                assertThat(suggest(input)).isEqualTo(expected)
            }
        }

    private fun suggest(input: String): Set<String> =
        projects.suggest(input = input, size = 100).map { it.label }.toSet()

    @Order(DELETE)
    @TestFactory
    fun `projects can be deleted by their ID`(): List<DynamicTest> {
        assertThatThereAreProjects()

        val deleteByIdTests = projects.getAll()
            .map { project ->
                dynamicTest("""delete "${project.label}"""") {
                    projects.delete(project.id)
                    assertThat(projects.get(project.id)).isNull()
                }
            }

        val getAllTest = dynamicTest("getting all projects returns empty result") {
            waitUntilSearchIndexIsRefreshed()
            assertThat(projects.getAll()).isEmpty()
        }

        return deleteByIdTests + getAllTest
    }

    @Test
    @Order(ACTUATOR)
    fun `actuator endpoint for triggering ReconstructProjectSearchIndexTask exists`() {
        projects.triggerSearchIndexReconstruction()
    }

    private fun waitUntilSearchIndexIsRefreshed() {
        searchIndex.refresh()
        sleep(2_000)
    }

    private fun assertThatThereAreProjects() {
        assertThat(projects.getAll()).isNotEmpty()
    }

}

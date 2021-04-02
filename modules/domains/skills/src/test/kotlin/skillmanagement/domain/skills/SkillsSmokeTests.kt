package skillmanagement.domain.skills

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
import skillmanagement.domain.skills.model.Skill
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
internal class SkillsSmokeTests(
    @Autowired private val searchIndexAdmin: SearchIndexAdmin<Skill>,
    @LocalServerPort port: Int
) {

    private val skills = SkillsTestDriver(port = port)

    @Order(CREATE)
    @TestFactory
    fun `skills can be created`(): List<DynamicTest> =
        listOf(
            dynamicTest("create 'Kotlin'") { skills.create(label = "Kotlin", tags = setOf("language")) },
            dynamicTest("create 'Kotlin Coroutines'") { skills.create(label = "Kotlin Coroutines") },
            dynamicTest("create 'Java'") { skills.create(label = "Java", tags = setOf("language")) },
            dynamicTest("create 'Python'") { skills.create(label = "Python", tags = setOf("language")) },
            dynamicTest("create 'Spring Boot'") { skills.create(label = "Spring Boot", tags = setOf("framework")) },
            dynamicTest("[wait for index refresh]") { waitUntilSearchIndexIsRefreshed() }
        )

    @Order(READ)
    @TestFactory
    fun `skills can be got by their ID`(): List<DynamicTest> =
        skills.getAll()
            .also { assertThat(it).isNotEmpty }
            .map { skill ->
                dynamicTest("get '${skill.label}'") {
                    assertThat(skills.get(skill.id)).isEqualTo(skill)
                }
            }

    @Order(READ)
    @TestFactory
    fun `getting all skills returns a paged result`() =
        listOf(
            0 to listOf("Java", "Kotlin", "Kotlin Coroutines"),
            1 to listOf("Python", "Spring Boot")
        ).map { (page, expected) ->
            dynamicTest("getAll page #$page >> $expected") {
                assertThat(getAllLabels(page = page)).isEqualTo(expected)
            }
        }

    private fun getAllLabels(page: Int): List<String> =
        skills.getAll(page = page, size = 3).map { it.label.toString() }

    @Order(READ)
    @TestFactory
    fun `searching for skills returns a paged result`() =
        listOf(
            "tags:language" to setOf("Java", "Kotlin", "Python"),
            "kot*" to setOf("Kotlin", "Kotlin Coroutines"),
            "boot" to setOf("Spring Boot")
        ).map { (query, expected) ->
            dynamicTest("query [$query] >> $expected") {
                assertThat(searchLabels(query)).isEqualTo(expected)
            }
        }

    private fun searchLabels(query: String): Set<String> =
        skills.search(query = query, page = 0, size = 100).map { it.label.toString() }.toSet()

    @Order(READ)
    @TestFactory
    fun `getting suggestions for skills returns their labels`() =
        listOf(
            "ko" to setOf("Kotlin", "Kotlin Coroutines"),
            "or" to setOf("Kotlin Coroutines"),
            "spring" to setOf("Spring Boot")
        ).map { (input, expected) ->
            dynamicTest("suggest for [$input] >> $expected") {
                assertThat(suggest(input)).isEqualTo(expected)
            }
        }

    private fun suggest(input: String): Set<String> =
        skills.suggest(input = input, size = 100).map { it.label }.toSet()

    @Order(DELETE)
    @TestFactory
    fun `skills can be deleted by their ID`(): List<DynamicTest> {
        assertThatThereAreSkills()

        val deleteByIdTests = skills.getAll()
            .map { skill ->
                dynamicTest("""delete "${skill.label}"""") {
                    skills.delete(skill.id)
                    assertThat(skills.get(skill.id)).isNull()
                }
            }

        val getAllTest = dynamicTest("getting all skills returns empty result") {
            waitUntilSearchIndexIsRefreshed()
            assertThat(skills.getAll()).isEmpty()
        }

        return deleteByIdTests + getAllTest
    }

    @Test
    @Order(ACTUATOR)
    fun `actuator endpoint for triggering ReconstructSkillSearchIndexTask exists`() {
        skills.triggerSearchIndexReconstruction()
    }

    private fun waitUntilSearchIndexIsRefreshed() {
        searchIndexAdmin.refresh()
        sleep(2_000)
    }

    private fun assertThatThereAreSkills() {
        assertThat(skills.getAll()).isNotEmpty()
    }

}

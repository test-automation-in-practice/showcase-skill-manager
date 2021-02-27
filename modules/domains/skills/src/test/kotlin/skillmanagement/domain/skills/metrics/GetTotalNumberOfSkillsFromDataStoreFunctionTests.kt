package skillmanagement.domain.skills.metrics

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotlintest.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import skillmanagement.domain.skills.model.skill_java
import skillmanagement.domain.skills.model.skill_kotlin
import skillmanagement.domain.skills.usecases.create.InsertSkillIntoDataStoreFunction
import skillmanagement.domain.skills.usecases.delete.DeleteSkillFromDataStoreFunction
import skillmanagement.test.TechnologyIntegrationTest

@JdbcTest
@AutoConfigureJson
@TestInstance(PER_CLASS)
@TechnologyIntegrationTest
internal class GetTotalNumberOfSkillsFromDataStoreFunctionTests(
    @Autowired val jdbcTemplate: NamedParameterJdbcTemplate,
    @Autowired val objectMapper: ObjectMapper
) {

    val insertSkillIntoDataStore = InsertSkillIntoDataStoreFunction(jdbcTemplate, objectMapper)
    val deleteSkillFromDataStore = DeleteSkillFromDataStoreFunction(jdbcTemplate)
    val getTotalNumberOfSkillsFromDataStore = GetTotalNumberOfSkillsFromDataStoreFunction(jdbcTemplate.jdbcTemplate)

    @Test
    fun `returns the total number of skill in the data store`() {
        getTotalNumberOfSkillsFromDataStore() shouldBe 0
        insertSkillIntoDataStore(skill_kotlin)
        getTotalNumberOfSkillsFromDataStore() shouldBe 1
        insertSkillIntoDataStore(skill_java)
        getTotalNumberOfSkillsFromDataStore() shouldBe 2
        deleteSkillFromDataStore(skill_java.id)
        getTotalNumberOfSkillsFromDataStore() shouldBe 1
    }

}

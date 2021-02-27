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
    @Autowired jdbcTemplate: NamedParameterJdbcTemplate,
    @Autowired objectMapper: ObjectMapper
) {

    private val delete = DeleteSkillFromDataStoreFunction(jdbcTemplate)
    private val insert = InsertSkillIntoDataStoreFunction(jdbcTemplate, objectMapper)
    private val getTotalNumberOfSkills = GetTotalNumberOfSkillsFromDataStoreFunction(jdbcTemplate.jdbcTemplate)

    @Test
    fun `returns the total number of skill in the data store`() {
        getTotalNumberOfSkills() shouldBe 0

        insert(skill_kotlin)
        getTotalNumberOfSkills() shouldBe 1

        insert(skill_java)
        getTotalNumberOfSkills() shouldBe 2

        delete(skill_java.id)
        getTotalNumberOfSkills() shouldBe 1
    }

}

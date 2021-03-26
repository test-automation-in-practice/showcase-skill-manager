package skillmanagement.domain.employees.searchindex

import org.assertj.core.api.Assertions.assertThat
import org.elasticsearch.client.RestHighLevelClient
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import skillmanagement.common.model.PageIndex
import skillmanagement.common.model.PageSize
import skillmanagement.common.model.Pagination
import skillmanagement.common.searchindices.MaxSuggestions
import skillmanagement.common.searchindices.PagedFindAllQuery
import skillmanagement.common.searchindices.PagedStringQuery
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.model.employee
import skillmanagement.domain.employees.model.employee_jane_doe
import skillmanagement.domain.employees.model.employee_john_doe
import skillmanagement.domain.employees.model.employee_john_smith
import skillmanagement.domain.employees.model.toSuggestion
import skillmanagement.test.searchindices.SearchIndexIntegrationTest
import java.util.UUID

@SearchIndexIntegrationTest
internal class EmployeeSearchIndexTests(client: RestHighLevelClient) {

    private val cut = EmployeeSearchIndex(client).apply { enabledTestMode() }

    @BeforeEach
    fun `reset search index`() {
        cut.reset()
    }

    @Test
    fun `entries can be deleted`() {
        val employee1 = index(employee_jane_doe).id
        val employee2 = index(employee_john_doe).id
        val employee3 = index(employee_john_smith).id

        assertIndexContainsOnly(employee1, employee2, employee3)

        delete(employee2)
        assertIndexContainsOnly(employee1, employee3)

        delete(employee1)
        assertIndexContainsOnly(employee3)

        delete(employee3)
        assertIndexIsEmpty()
    }

    @Nested
    inner class QueryOperation {

        @Test
        fun `querying an empty index returns empty result`() {
            assertThat(query("foo")).isEmpty()
        }

        @Test
        fun `query can be used to search employee names`() {
            val janeDoe = index(employee(firstName = "Jane", lastName = "Doe")).id
            val johnDoe = index(employee(firstName = "John", lastName = "Doe")).id
            val johnSmith = index(employee(firstName = "John", lastName = "Smith")).id

            assertThat(query("Doe")).containsOnly(janeDoe, johnDoe)
            assertThat(query("firstName:john AND lastName:d*")).containsOnly(johnDoe)
            assertThat(query("firstName:jo*")).containsOnly(johnDoe, johnSmith)
        }

        @Test
        fun `query results are paged`() {
            val employee1 = index(employee(firstName = "Employee", lastName = "1")).id
            val employee2 = index(employee(firstName = "Employee", lastName = "2")).id
            val employee3 = index(employee(firstName = "Employee", lastName = "3")).id
            val employee4 = index(employee(firstName = "Employee", lastName = "4")).id
            val employee5 = index(employee(firstName = "Employee", lastName = "5")).id

            assertThat(query("employee"))
                .containsOnly(employee1, employee2, employee3, employee4, employee5)

            val page1 = query("employee", pageIndex = 0, pageSize = 2)
            val page2 = query("employee", pageIndex = 1, pageSize = 2)
            val page3 = query("employee", pageIndex = 2, pageSize = 2)

            assertThat(page1).hasSize(2)
            assertThat(page2).hasSize(2)
            assertThat(page3).hasSize(1)

            assertThat(page1 + page2 + page3)
                .containsOnly(employee1, employee2, employee3, employee4, employee5)
        }

        @Test
        fun `query only uses combined names by default`() {
            val employee = index(employee(firstName = "Jane", lastName = "Doe", title = "title")).id

            assertThat(query("jane")).containsOnly(employee)
            assertThat(query("title")).isEmpty()
        }

    }

    @Nested
    inner class FindAllOperation {

        @Test
        fun `find all for an empty index returns empty result`() {
            assertThat(findAll()).isEmpty()
        }

        @Test
        fun `find all returns all employees paged and sorted by label`() {
            val employeeA = index(employee(firstName = "Employee", lastName = "A")).id
            val employeeB = index(employee(firstName = "Employee", lastName = "B")).id
            val employeeC = index(employee(firstName = "Employee", lastName = "C")).id
            val employeeD = index(employee(firstName = "Employee", lastName = "D")).id
            val employeeE = index(employee(firstName = "Employee", lastName = "E")).id
            val employeeF = index(employee(firstName = "Employee", lastName = "F")).id
            val employeeG = index(employee(firstName = "Employee", lastName = "G")).id

            assertThat(findAll())
                .containsExactly(employeeA, employeeB, employeeC, employeeD, employeeE, employeeF, employeeG)

            assertThat(findAll(pageIndex = 0, pageSize = 3)).containsExactly(employeeA, employeeB, employeeC)
            assertThat(findAll(pageIndex = 1, pageSize = 3)).containsExactly(employeeD, employeeE, employeeF)
            assertThat(findAll(pageIndex = 2, pageSize = 3)).containsExactly(employeeG)
        }

    }

    @Nested
    inner class SuggestOperation {

        @Test
        fun `getting suggestions from an empty index returns empty result`() {
            assertThat(query("foo")).isEmpty()
        }

        @Test
        fun `getting suggestions returns employees with matching names`() {
            val employee1 = index(employee(firstName = "Jane", lastName = "Doe")).toSuggestion()
            val employee2 = index(employee(firstName = "John", lastName = "Doe")).toSuggestion()
            val employee3 = index(employee(firstName = "John", lastName = "Smith")).toSuggestion()
            val employee4 = index(employee(firstName = "Jane", lastName = "Smith")).toSuggestion()

            assertThat(suggest("joh"))
                .contains(employee3, employee3)
                .doesNotContain(employee1, employee4)

            assertThat(suggest("j", max = 2)).hasSize(2)
        }

        @Test
        fun `getting suggestions only uses name data`() {
            val employee = index(employee(firstName = "Jane", lastName = "Doe", title = "title")).toSuggestion()

            assertThat(suggest("ja")).containsOnly(employee)
            assertThat(suggest("do")).containsOnly(employee)
            assertThat(suggest("ti")).isEmpty()
        }

    }

    private fun index(employee: Employee): Employee = employee.also { cut.index(it) }
    private fun delete(vararg ids: UUID) = ids.forEach(cut::deleteById)

    private fun assertIndexContainsOnly(vararg ids: UUID) {
        assertThat(findAll()).containsExactly(*ids)
    }

    private fun assertIndexIsEmpty() {
        assertThat(findAll()).isEmpty()
    }

    private fun query(
        query: String,
        pageIndex: Int = PageIndex.DEFAULT.toInt(),
        pageSize: Int = PageSize.DEFAULT.toInt()
    ) = cut.query(SimplePagedStringQuery(query, Pagination(PageIndex(pageIndex), PageSize(pageSize))))

    private fun findAll(
        pageIndex: Int = PageIndex.DEFAULT.toInt(),
        pageSize: Int = PageSize.DEFAULT.toInt()
    ) = cut.findAll(SimplePagedFindAllQuery(Pagination(PageIndex(pageIndex), PageSize(pageSize))))

    private fun suggest(
        input: String,
        max: Int = MaxSuggestions.DEFAULT.toInt()
    ) = cut.suggest(input, MaxSuggestions(max))

    private data class SimplePagedStringQuery(
        override val queryString: String,
        override val pagination: Pagination,
    ) : PagedStringQuery

    private data class SimplePagedFindAllQuery(
        override val pagination: Pagination
    ) : PagedFindAllQuery
}

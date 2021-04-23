package skillmanagement.domain.employees.usecases.update

import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import skillmanagement.common.success
import skillmanagement.domain.employees.model.EmployeeEntity
import skillmanagement.domain.employees.model.FirstName
import skillmanagement.domain.employees.model.employee_jane_doe
import skillmanagement.test.UnitTest

@UnitTest
internal class UpdateEmployeeByIdFunctionTests {

    private val updateEmployeeEntityById: UpdateEmployeeEntityByIdFunction = mockk()
    private val updateEmployeeById = UpdateEmployeeByIdFunction(updateEmployeeEntityById)

    private val entity = employee_jane_doe

    @Test
    fun `delegates update to entity updater`() {
        every { updateEmployeeEntityById(entity.id, any()) } answers { simulateUpdate(secondArg()) }

        val actual = updateEmployeeById(entity.id) { it.copy(firstName = FirstName("Testee")) }
        val expected = success(entity.copy(data = entity.data.copy(firstName = FirstName("Testee"))))
        assertThat(actual).isEqualTo(expected)
    }

    private fun simulateUpdate(block: (EmployeeEntity) -> EmployeeEntity) =
        success(block(entity))

}

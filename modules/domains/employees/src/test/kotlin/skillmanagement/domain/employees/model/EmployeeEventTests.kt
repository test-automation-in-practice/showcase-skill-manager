package skillmanagement.domain.employees.model

import org.junit.jupiter.api.Nested
import skillmanagement.test.AbstractJsonSerializationTests
import skillmanagement.test.TechnologyIntegrationTest

internal class EmployeeEventTests {

    @Nested
    @TechnologyIntegrationTest
    inner class JsonSerializationTests : AbstractJsonSerializationTests<EmployeeEvent>() {
        override val serializationExamples = listOf(
            EmployeeAddedEvent(employee_jane_doe) to """{ "employee": $employee_jane_doe_json }""",
            EmployeeUpdatedEvent(employee_john_doe) to """{ "employee": $employee_john_doe_json }""",
            EmployeeDeletedEvent(employee_john_smith) to """{ "employee": $employee_john_smith_json }"""
        )
    }

}

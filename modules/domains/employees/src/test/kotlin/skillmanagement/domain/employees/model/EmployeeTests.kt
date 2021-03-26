package skillmanagement.domain.employees.model

import org.junit.jupiter.api.Nested
import skillmanagement.test.AbstractJsonSerializationTests
import skillmanagement.test.TechnologyIntegrationTest

internal class EmployeeTests {

    @Nested
    @TechnologyIntegrationTest
    inner class JsonSerializationTests : AbstractJsonSerializationTests<Employee>() {
        override val serializationExamples = listOf(
            employee_jane_doe to employee_jane_doe_json,
            employee_john_doe to employee_john_doe_json,
            employee_john_smith to employee_john_smith_json
        )
    }

}

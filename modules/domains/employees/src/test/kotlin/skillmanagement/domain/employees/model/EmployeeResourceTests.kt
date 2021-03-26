package skillmanagement.domain.employees.model

import org.junit.jupiter.api.Nested
import skillmanagement.test.AbstractJsonSerializationTests
import skillmanagement.test.TechnologyIntegrationTest

internal class EmployeeResourceTests {

    @Nested
    @TechnologyIntegrationTest
    inner class JsonSerializationTests : AbstractJsonSerializationTests<EmployeeResource>() {
        override val serializationExamples = listOf(
            employee_resource_jane_doe to employee_resource_jane_doe_json,
            employee_resource_john_doe to employee_resource_john_doe_json,
            employee_resource_john_smith to employee_resource_john_smith_json,
        )
    }

}

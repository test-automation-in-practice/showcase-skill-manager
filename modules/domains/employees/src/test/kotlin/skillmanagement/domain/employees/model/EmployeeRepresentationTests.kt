package skillmanagement.domain.employees.model

import org.junit.jupiter.api.Nested
import skillmanagement.test.AbstractJsonSerializationTests
import skillmanagement.test.TechnologyIntegrationTest

internal class EmployeeRepresentationTests {

    @Nested
    @TechnologyIntegrationTest
    inner class JsonSerializationTests : AbstractJsonSerializationTests<EmployeeRepresentation>() {
        override val serializationExamples = listOf(
            employee_representation_jane_doe to employee_representation_jane_doe_json,
            employee_representation_john_doe to employee_representation_john_doe_json,
            employee_representation_john_smith to employee_representation_john_smith_json,
        )
    }

}

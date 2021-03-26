package skillmanagement.domain.employees.model

import org.junit.jupiter.api.Nested
import skillmanagement.test.AbstractJsonSerializationTests
import skillmanagement.test.TechnologyIntegrationTest

internal class EmployeeCreationDataTests {

    @Nested
    @TechnologyIntegrationTest
    inner class JsonSerializationTests : AbstractJsonSerializationTests<EmployeeCreationData>() {
        override val serializationExamples = listOf(
            employee_creation_data_jane_doe to employee_creation_data_jane_doe_json,
            employee_creation_data_john_doe to employee_creation_data_john_doe_json,
            employee_creation_data_john_smith to employee_creation_data_john_smith_json
        )
    }

}

package skillmanagement.domain.employees.tasks

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.testit.testutils.logrecorder.api.LogRecord
import org.testit.testutils.logrecorder.junit5.RecordLoggers
import skillmanagement.common.searchindices.SearchIndexAdmin
import skillmanagement.domain.employees.model.EmployeeEntity
import skillmanagement.domain.employees.model.employee_jane_doe
import skillmanagement.domain.employees.model.employee_john_doe
import skillmanagement.domain.employees.model.employee_john_smith
import skillmanagement.domain.employees.usecases.read.GetEmployeesFromDataStoreFunction
import skillmanagement.test.ResetMocksAfterEachTest
import skillmanagement.test.UnitTest
import skillmanagement.test.tasks.AbstractReconstructSearchIndexTaskTests

@UnitTest
@ResetMocksAfterEachTest
internal class ReconstructEmployeeSearchIndexTaskTests : AbstractReconstructSearchIndexTaskTests<EmployeeEntity>() {

    private val getEmployeesFromDataStore: GetEmployeesFromDataStoreFunction = mockk()

    override val searchIndexAdmin: SearchIndexAdmin<EmployeeEntity> = mockk(relaxUnitFun = true)
    override val cut = ReconstructEmployeeSearchIndexTask(searchIndexAdmin, getEmployeesFromDataStore)

    override val instance1 = employee_jane_doe
    override val instance2 = employee_john_doe
    override val instance3 = employee_john_smith

    @Test
    @RecordLoggers(ReconstructEmployeeSearchIndexTask::class)
    override fun `task steps are logged`(log: LogRecord) = executeTaskStepsAreLoggedTest(log)

    override fun stubDataStoreToBeEmpty() = stubDataStoreToContain(emptyList())

    override fun stubDataStoreToContain(instances: Collection<EmployeeEntity>) {
        every { getEmployeesFromDataStore(any<(EmployeeEntity) -> Unit>()) } answers {
            instances.forEach { firstArg<(EmployeeEntity) -> Unit>()(it) }
        }
    }

    override fun expectedShortDescription(instance: EmployeeEntity) = "${instance.id} - ${instance.compositeName()}"

}

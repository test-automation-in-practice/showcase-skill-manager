package skillmanagement.domain.employees.tasks

import mu.KotlinLogging.logger
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint
import org.springframework.stereotype.Component
import skillmanagement.common.searchindices.AbstractReconstructSearchIndexTask
import skillmanagement.common.searchindices.SearchIndexAdmin
import skillmanagement.common.stereotypes.Task
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.usecases.read.GetEmployeesFromDataStoreFunction

@Component
@WebEndpoint(id = "reconstructEmployeeSearchIndex")
internal class ReconstructEmployeeSearchIndexTaskWebEndpoint(
    private val task: ReconstructEmployeeSearchIndexTask
) {

    @WriteOperation
    fun trigger() = task.run()

}

@Task
internal class ReconstructEmployeeSearchIndexTask(
    override val searchIndexAdmin: SearchIndexAdmin<Employee>,
    private val getEmployeeFromDataStore: GetEmployeesFromDataStoreFunction
) : AbstractReconstructSearchIndexTask<Employee>() {

    override val log = logger {}

    override fun executeForAllInstancesInDataStore(callback: (Employee) -> Unit) = getEmployeeFromDataStore(callback)
    override fun shortDescription(instance: Employee) = "${instance.id} - ${instance.firstName} ${instance.lastName}"

}

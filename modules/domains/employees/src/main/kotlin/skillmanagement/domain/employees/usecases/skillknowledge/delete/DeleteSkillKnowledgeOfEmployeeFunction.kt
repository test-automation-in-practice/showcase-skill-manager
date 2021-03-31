package skillmanagement.domain.employees.usecases.skillknowledge.delete

import arrow.core.Either
import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.employees.model.Employee
import skillmanagement.domain.employees.usecases.skillknowledge.delete.DeletionFailure.EmployeeNotFound
import skillmanagement.domain.employees.usecases.skillknowledge.delete.DeletionFailure.SkillKnowledgeNotFound
import skillmanagement.domain.employees.usecases.update.EmployeeUpdateFailure
import skillmanagement.domain.employees.usecases.update.UpdateEmployeeByIdFunction
import java.util.UUID

@BusinessFunction
class DeleteSkillKnowledgeOfEmployeeFunction internal constructor(
    private val updateEmployeeById: UpdateEmployeeByIdFunction
) {

    operator fun invoke(employeeId: UUID, skillId: UUID): Either<DeletionFailure, Employee> {
        val updateResult = updateEmployeeById(employeeId) {
            it.removeSkillKnowledgeBySkillId(skillId)
        }

        return updateResult.mapLeft { failure ->
            when (failure) {
                is EmployeeUpdateFailure.EmployeeNotFound -> EmployeeNotFound
                is EmployeeUpdateFailure.EmployeeNotChanged -> SkillKnowledgeNotFound
            }
        }
    }

}

sealed class DeletionFailure {
    object EmployeeNotFound : DeletionFailure()
    object SkillKnowledgeNotFound : DeletionFailure()
}

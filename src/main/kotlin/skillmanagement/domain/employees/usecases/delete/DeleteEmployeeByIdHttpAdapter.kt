package skillmanagement.domain.employees.usecases.delete

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import skillmanagement.domain.HttpAdapter
import java.util.UUID

@HttpAdapter
@RequestMapping("/api/employees/{id}")
class DeleteEmployeeByIdHttpAdapter(
    private val deleteEmployeeById: DeleteEmployeeById
) {

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: UUID) {
        deleteEmployeeById(id)
    }

}

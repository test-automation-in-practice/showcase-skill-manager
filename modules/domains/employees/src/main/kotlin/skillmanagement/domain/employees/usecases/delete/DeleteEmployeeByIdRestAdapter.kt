package skillmanagement.domain.employees.usecases.delete

import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import skillmanagement.common.stereotypes.RestAdapter
import java.util.UUID

@RestAdapter
@RequestMapping("/api/employees/{id}")
internal class DeleteEmployeeByIdRestAdapter(
    private val deleteEmployeeById: DeleteEmployeeByIdFunction
) {

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    fun delete(@PathVariable id: UUID) {
        deleteEmployeeById(id)
    }

}

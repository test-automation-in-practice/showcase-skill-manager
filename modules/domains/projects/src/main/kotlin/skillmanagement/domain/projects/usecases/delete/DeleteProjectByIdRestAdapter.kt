package skillmanagement.domain.projects.usecases.delete

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import skillmanagement.common.stereotypes.RestAdapter
import java.util.UUID

@RestAdapter
@RequestMapping("/api/projects/{id}")
internal class DeleteProjectByIdRestAdapter(
    private val deleteProjectById: DeleteProjectByIdFunction
) {

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: UUID) {
        deleteProjectById(id)
    }

}

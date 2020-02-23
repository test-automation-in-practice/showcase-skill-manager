package skillmanagement.domain.projects.delete

import skillmanagement.domain.BusinessFunction
import java.util.*

@BusinessFunction
class DeleteProjectById(
    private val deleteProjectFromDataStore: DeleteProjectFromDataStore
) {

    // TODO: Security - Only invokable by Project-Admins
    operator fun invoke(id: UUID) {
        deleteProjectFromDataStore(id)
        // TODO: Remove projects from linked employees? Maybe with an event?
    }

}

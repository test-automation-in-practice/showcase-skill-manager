package skillmanagement.domain.projects.get

import skillmanagement.domain.BusinessFunction
import skillmanagement.domain.projects.Project
import java.util.UUID

@BusinessFunction
class GetProjectById(
    private val getProjectFromDataStore: GetProjectFromDataStore
) {

    operator fun invoke(id: UUID): Project? {
        return getProjectFromDataStore(id)
    }

}

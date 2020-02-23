package skillmanagement.domain.projects.get

import skillmanagement.domain.projects.Project
import skillmanagement.domain.BusinessFunction
import java.util.*

@BusinessFunction
class GetProjectById(
    private val getProjectFromDataStore: GetProjectFromDataStore
) {

    operator fun invoke(id: UUID): Project? {
        return getProjectFromDataStore(id)
    }

}

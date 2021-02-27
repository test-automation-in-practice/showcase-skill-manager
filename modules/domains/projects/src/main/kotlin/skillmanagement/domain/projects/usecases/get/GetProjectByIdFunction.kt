package skillmanagement.domain.projects.usecases.get

import skillmanagement.common.stereotypes.BusinessFunction
import skillmanagement.domain.projects.model.Project
import java.util.UUID

@BusinessFunction
class GetProjectByIdFunction(
    private val getProjectsFromDataStore: GetProjectsFromDataStoreFunction
) {

    operator fun invoke(id: UUID): Project? {
        return getProjectsFromDataStore(id)
    }

}

package skillmanagement.domain.projects.get

import skillmanagement.domain.projects.Project
import skillmanagement.domain.BusinessFunction
import java.util.*

@BusinessFunction
class GetProjectsByIds(
    private val getProjectFromDataStore: GetProjectFromDataStore
) {

    operator fun invoke(ids: Collection<UUID>): Map<UUID, Project> {
        return getProjectFromDataStore(ids)
            .map { it.id to it }
            .toMap()
    }

}


package skillmanagement.common.model

import java.time.Instant

interface Entity<ID : IdType> {
    val id: ID
    val version: Int
    val data: Any
    val created: Instant
    val lastUpdate: Instant
}

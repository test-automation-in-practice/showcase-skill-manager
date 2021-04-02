package skillmanagement.common.model

import java.util.UUID

/**
 * [ValueType] for [UUID] values.
 */
open class IdType(value: UUID) : ValueType<UUID>(value) {
    fun toUUID() = value
}

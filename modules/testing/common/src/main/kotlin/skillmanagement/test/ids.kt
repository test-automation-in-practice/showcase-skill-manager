package skillmanagement.test

import org.springframework.util.IdGenerator
import java.util.ArrayDeque
import java.util.Deque
import java.util.UUID

fun uuid(): UUID = UUID.randomUUID()
fun uuid(value: String): UUID = UUID.fromString(value)

/**
 * Creates an [IdGenerator] which will return a pre defined sequence of IDs.
 *
 * If all defined IDs are used up, an exception will be thrown.
 */
fun sequentialIdGenerator(vararg ids: String): IdGenerator =
    SequentialIdGenerator(ids.map(UUID::fromString))

private class SequentialIdGenerator(ids: Collection<UUID>) : IdGenerator {
    private val queue: Deque<UUID> = ArrayDeque(ids)
    override fun generateId(): UUID = queue.pop()
}

package skillmanagement.test

import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.util.ArrayDeque
import java.util.Deque

fun instant(value: String): Instant = Instant.parse(value)

fun localDate(value: String): LocalDate = LocalDate.parse(value)

fun yearMonth(value: String): YearMonth = YearMonth.parse(value)

/**
 * Creates a [Clock] which will return the same timestamps every time
 * it is invoked.
 */
fun fixedClock(timestamps: String): Clock =
    Clock.fixed(Instant.parse(timestamps), ZoneId.of("UTC"))

/**
 * Creates a [Clock] which will return a pre defined sequence of timestamps.
 *
 * If all defined timestamps are used up, an exception will be thrown.
 */
fun sequentialClock(vararg timestamps: String): Clock =
    SequentialClock(timestamps.map(Instant::parse))

private class SequentialClock(timestamps: Collection<Instant>) : Clock() {

    private val queue: Deque<Instant> = ArrayDeque(timestamps)

    override fun instant(): Instant = queue.pop()

    override fun withZone(zone: ZoneId?): Clock = error("not supported")
    override fun getZone(): ZoneId = error("not supported")

}

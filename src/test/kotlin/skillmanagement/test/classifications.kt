package skillmanagement.test

import org.springframework.test.annotation.DirtiesContext
import kotlin.annotation.AnnotationTarget.CLASS
import net.jqwik.api.Tag as JQwikTag
import org.junit.jupiter.api.Tag as JUnitTag

@Retention
@Target(CLASS)
@JUnitTag("unit-test")
@JQwikTag("unit-test")
annotation class UnitTest

@Retention
@Target(CLASS)
@DirtiesContext
@JUnitTag("integration-test")
@JQwikTag("integration-test")
annotation class TechnologyIntegrationTest

@Retention
@Target(CLASS)
@JUnitTag("end2end-test")
@JQwikTag("end2end-test")
annotation class End2EndTest

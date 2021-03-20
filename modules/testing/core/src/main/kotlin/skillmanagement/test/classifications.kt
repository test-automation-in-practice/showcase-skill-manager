package skillmanagement.test

import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import kotlin.annotation.AnnotationTarget.CLASS
import net.jqwik.api.Tag as JQwikTag
import org.junit.jupiter.api.Tag as JUnitTag

private const val TEST = "test"
private const val UNIT_TEST = "unit-test"
private const val INTEGRATION_TEST = "integration-test"
private const val END2END_TEST = "end2end-test"

@Retention
@Target(CLASS)
@JUnitTag(UNIT_TEST)
@JQwikTag(UNIT_TEST)
annotation class UnitTest

@Retention
@Target(CLASS)
@DirtiesContext
@JUnitTag(INTEGRATION_TEST)
@JQwikTag(INTEGRATION_TEST)
@ActiveProfiles(TEST, INTEGRATION_TEST)
annotation class TechnologyIntegrationTest

/**
 * Smoke-Tests are used to verify core functionality at a very high level of
 * abstraction.
 *
 * They consist of a handful of "happy path" test scenarios for the most
 * important operations of the System Under Test (SUT). In comparison to other
 * types of testing, Smoke-Tests ensure that no integration problems have
 * arisen during the decomposition of business and technology code that were
 * not detectable at finer test levels (e.g. Unit-Tests).
 *
 * **Definition:**
 *
 * In computer programming and software testing, smoke testing (also confidence
 * testing, sanity testing, build verification test (BVT) and build acceptance
 * test) is preliminary testing to reveal simple failures severe enough to, for
 * example, reject a prospective software release. Smoke tests are a subset of
 * test cases that cover the most important functionality of a component or
 * system, used to aid assessment of whether main functions of the software
 * appear to work correctly.
 *
 * -- https://en.wikipedia.org/wiki/Smoke_testing_(software)
 */
@Retention
@Target(CLASS)
@JUnitTag(END2END_TEST)
@JQwikTag(END2END_TEST)
@ActiveProfiles(TEST, END2END_TEST)
annotation class SmokeTest

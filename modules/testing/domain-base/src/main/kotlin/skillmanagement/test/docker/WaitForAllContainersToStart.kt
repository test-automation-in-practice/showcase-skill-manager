package skillmanagement.test.docker

import org.junit.jupiter.api.extension.ExtendWith

@Retention
@Target(AnnotationTarget.CLASS)
@ExtendWith(WaitForAllContainersToStartExtension::class)
annotation class WaitForAllContainersToStart

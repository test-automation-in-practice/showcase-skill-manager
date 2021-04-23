package skillmanagement.runtime

import org.junit.jupiter.api.Test
import skillmanagement.test.SmokeTest
import skillmanagement.test.e2e.SpringBootTestWithDockerizedDependencies

@SmokeTest
@SpringBootTestWithDockerizedDependencies
class ApplicationSmokeTests {

    @Test
    fun `application context can be loaded`() {
        // do nothing
    }

}

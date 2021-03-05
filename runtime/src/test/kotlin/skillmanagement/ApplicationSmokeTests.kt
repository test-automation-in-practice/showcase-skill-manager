package skillmanagement

import org.junit.jupiter.api.Test
import skillmanagement.test.SmokeTest
import skillmanagement.test.SpringBootTestWithDockerizedDependencies

@SmokeTest
@SpringBootTestWithDockerizedDependencies
class ApplicationSmokeTests {

    @Test
    fun `application context can be loaded`() {
        // do nothing
    }

}

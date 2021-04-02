package skillmanagement.domain.projects.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import skillmanagement.test.UnitTest

@UnitTest
internal class StringToProjectIdConverterTests {

    private val cut = StringToProjectIdConverter()

    @ParameterizedTest
    @ValueSource(strings = ["e8842e33-4ba4-47ab-9dea-0d60032063d9", "5fe42a8f-055d-4ca6-89f2-3fc69e2e4e6e"])
    fun `coverts valid values`(uuid: String) {
        assertThat(cut.convert(uuid)).isEqualTo(projectId(uuid))
    }

    @Test
    fun `throws correct exception for invalid values`() {
        assertThrows<IllegalArgumentException> {
            cut.convert("foo-bar")
        }
    }

}

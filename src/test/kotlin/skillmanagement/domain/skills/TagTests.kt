package skillmanagement.domain.skills

import skillmanagement.test.UnitTest
import skillmanagement.test.contracts.IsJsonSerializableStringTypeContract
import skillmanagement.test.contracts.MatchesPatternStringTypeContract

@UnitTest
internal class TagTests : MatchesPatternStringTypeContract, IsJsonSerializableStringTypeContract {

    override val validExampleValues = listOf("language", "cool", "foo-bar", "bar_foo", "a")
    override val invalidExampleValues = listOf("Language", "foo bar", "äöi", "FOO")

    override fun createInstance(value: String) = Tag(value)

}

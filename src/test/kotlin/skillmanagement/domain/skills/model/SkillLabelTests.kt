package skillmanagement.domain.skills.model

import skillmanagement.test.UnitTest
import skillmanagement.test.contracts.HasMaxLengthOfStringTypeContract
import skillmanagement.test.contracts.IsJsonSerializableStringTypeContract
import skillmanagement.test.contracts.IsNotBlankStringTypeContract
import skillmanagement.test.stringOfLength

@UnitTest
internal class SkillLabelTests :
    IsNotBlankStringTypeContract, HasMaxLengthOfStringTypeContract, IsJsonSerializableStringTypeContract {

    override val maxLength: Int = 75
    override val validExampleValues = listOf("Kotlin", "Java", "Moderation", "Presentation", "Jenkins")

    override fun createInstance(value: String) = SkillLabel(value)
    override fun createInstanceOfLength(length: Int) = createInstance(stringOfLength(length))

}

package skillmanagement.test.contracts.string

import skillmanagement.common.model.DEFAULT_MAX_TEXT_LENGTH

abstract class TextTypeContract : IsNotBlankContract, HasMaxLengthContract, IsJsonSerializableContract {
    override val maxLength: Int = DEFAULT_MAX_TEXT_LENGTH
}

package skillmanagement.test.contracts.string

import skillmanagement.common.model.DEFAULT_MAX_LABEL_LENGTH

abstract class LabelTypeContract : IsNotBlankContract, HasMaxLengthContract, IsJsonSerializableContract {
    override val maxLength: Int = DEFAULT_MAX_LABEL_LENGTH
}

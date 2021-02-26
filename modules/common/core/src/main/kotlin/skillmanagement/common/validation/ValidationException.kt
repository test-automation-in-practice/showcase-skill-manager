package skillmanagement.common.validation

class ValidationException(label: String, val problems: List<String>) :
    RuntimeException("Value of $label is invalid: ${problems.joinToString(separator = "; ")}")

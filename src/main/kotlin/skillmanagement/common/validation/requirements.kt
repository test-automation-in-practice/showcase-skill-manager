package skillmanagement.common.validation

fun Validation<Int>.isGreaterThanOrEqualTo(minValue: Int) {
    if (value < minValue) {
        addProblem("must be greater than or equal to $minValue!")
    }
}

fun Validation<Int>.isLessThanOrEqualTo(maxValue: Int) {
    if (value > maxValue) {
        addProblem("must be less than or equal to $maxValue!")
    }
}

fun Validation<String>.isNotBlank() {
    if (value.isBlank()) {
        addProblem("must not be blank!")
    }
}

fun Validation<String>.matchesPattern(pattern: Regex) {
    if (!value.matches(pattern)) {
        addProblem("must match pattern: $pattern")
    }
}

fun Validation<String>.hasMaxLengthOf(maxLength: Int) {
    if (value.length > maxLength) {
        addProblem("must not be longer than $maxLength characters, but is ${value.length} characters long!")
    }
}

fun Validation<String>.doesNotContainAny(characters: Iterable<Char>) {
    if (value.any { char -> characters.contains(char) }) {
        addProblem("contains at least one of the following illegal characters: ${characters.joinToString(" ")}")
    }
}

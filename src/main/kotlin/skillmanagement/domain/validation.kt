package skillmanagement.domain

import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.web.bind.annotation.ResponseStatus

class Validation<T>(
    private val label: String,
    val value: T
) {

    private val problems: MutableList<String> = mutableListOf()

    companion object {
        fun <T> validate(value: T, label: String, block: Validation<T>.() -> Unit) {
            Validation(label, value)
                .apply(block)
                .validate()
        }
    }

    fun addProblem(problem: String) {
        problems.add("'$label' [$value] - $problem")
    }

    private fun validate() {
        if (problems.isNotEmpty()) throw ValidationException(label, problems)
    }

}

@ResponseStatus(BAD_REQUEST)
class ValidationException(label: String, val problems: List<String>) :
    RuntimeException("Value of $label is invalid: ${problems.joinToString(separator = "; ")}")

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

package skillmanagement.common.validation

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
        if (problems.isNotEmpty()) throw ValidationException(
            label,
            problems
        )
    }

}

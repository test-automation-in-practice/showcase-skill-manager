package skillmanagement.test


val lowerCaseLetters = charArrayOf(
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
    'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
)

val upperCaseLetters = charArrayOf(
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
    'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
)

private val defaultAlphabet = (lowerCaseLetters + upperCaseLetters).toList()

fun stringOfLength(length: Int, alphabet: Collection<Char> = defaultAlphabet) = StringBuilder()
    .apply { repeat(length) { append(alphabet.random()) } }
    .toString()

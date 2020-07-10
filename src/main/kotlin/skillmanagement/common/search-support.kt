package skillmanagement.common

import java.text.Normalizer.Form.NFD
import java.text.Normalizer.normalize

private val accents = Regex("""\p{InCombiningDiacriticalMarks}+""")
private val nonLetterOrNumberCharacter = Regex("[^a-z0-9]")

internal fun searchTerms(value: String): List<String> = value
    .toLowerCase()
    .split(Regex("\\s+"))
    .asSequence()
    .map { it.trim() }
    .map { normalize(it, NFD) }
    .map { it.replace(accents, "") }
    .map { it.replace(nonLetterOrNumberCharacter, "") }
    .filter { it.isNotEmpty() }
    .toList()

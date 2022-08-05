package skillmanagement.test

import org.springframework.core.io.ClassPathResource
import java.io.BufferedReader

fun ClassPathResource.readText(): String =
    inputStream.bufferedReader().use(BufferedReader::readText)

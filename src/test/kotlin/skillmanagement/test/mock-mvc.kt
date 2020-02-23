package skillmanagement.test

import org.springframework.test.web.servlet.result.ContentResultMatchers

fun ContentResultMatchers.relaxedJson(jsonSupplier: () -> String) = json(jsonSupplier(), false)

fun ContentResultMatchers.strictJson(jsonSupplier: () -> String) = json(jsonSupplier(), true)

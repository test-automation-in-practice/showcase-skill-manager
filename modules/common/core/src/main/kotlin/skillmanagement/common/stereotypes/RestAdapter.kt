package skillmanagement.common.stereotypes

import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController
import kotlin.annotation.AnnotationTarget.CLASS

@Retention
@Target(CLASS)
@Transactional
@RestController
annotation class RestAdapter

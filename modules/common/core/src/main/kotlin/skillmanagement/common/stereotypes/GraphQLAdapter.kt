package skillmanagement.common.stereotypes

import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import kotlin.annotation.AnnotationTarget.CLASS

@Retention
@Controller
@Target(CLASS)
@Transactional
annotation class GraphQLAdapter

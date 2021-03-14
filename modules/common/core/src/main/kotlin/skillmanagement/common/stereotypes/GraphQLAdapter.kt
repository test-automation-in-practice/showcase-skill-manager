package skillmanagement.common.stereotypes

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import kotlin.annotation.AnnotationTarget.CLASS

@Component
@Retention
@Target(CLASS)
@Transactional
annotation class GraphQLAdapter

package skillmanagement.domain

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.ResponseBody
import kotlin.annotation.AnnotationTarget.CLASS

@Controller
@ResponseBody
@Retention
@Target(CLASS)
annotation class HttpAdapter

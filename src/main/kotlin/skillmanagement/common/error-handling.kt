package skillmanagement.common

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import com.fasterxml.jackson.databind.exc.ValueInstantiationException
import mu.KotlinLogging.logger
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import skillmanagement.domain.ValidationException
import java.time.Clock
import java.time.Instant
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class GlobalRestControllerAdvice(
    private val clock: Clock
) {

    private val log = logger {}

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handle(e: HttpMessageNotReadableException, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val cause = (e.cause as? ValueInstantiationException)?.cause
        if (cause is ValidationException) {
            val body = errorResponse(
                clock = clock,
                request = request,
                status = BAD_REQUEST,
                message = "Request body validation failed!",
                details = cause.problems
            )
            log.info(e) { "Received bad request, responding with: $body" }
            return ResponseEntity.badRequest().body(body)
        }
        throw e
    }

}

fun errorResponse(
    clock: Clock,
    request: HttpServletRequest,
    status: HttpStatus,
    message: String?,
    details: List<String>? = null
) = ErrorResponse(
    timestamp = clock.instant(),
    status = status.value(),
    error = status.reasonPhrase,
    path = request.requestURI,
    message = message,
    details = details
)

@JsonInclude(NON_NULL)
data class ErrorResponse(
    val timestamp: Instant,
    val status: Int,
    val path: String,
    val error: String,
    val message: String?,
    val details: List<String>? = null
)

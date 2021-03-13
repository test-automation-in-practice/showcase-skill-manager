package skillmanagement.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.stereotype.Component

@ConstructorBinding
@ConfigurationProperties(prefix = "custom.security.cors")
data class CorsConfigurationProperties(
    val allowedOrigins: List<String>
)

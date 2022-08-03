package skillmanagement.runtime.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("security.users")
data class UsersProperties(
    val basicAuth: List<BasicAuthUser>
) {

    @ConstructorBinding
    data class BasicAuthUser(
        val username: String,
        val password: String,
        val authorities: Set<String>
    )

}

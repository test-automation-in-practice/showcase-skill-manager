package skillmanagement.runtime.security

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest
import org.springframework.boot.actuate.info.InfoEndpoint
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.config.web.servlet.HttpSecurityDsl
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import skillmanagement.runtime.security.Authorities.SCOPE_ACTUATOR
import skillmanagement.runtime.security.Authorities.SCOPE_API
import skillmanagement.runtime.security.Authorities.SCOPE_GRAPHIQL

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(UsersProperties::class)
class WebSecurityConfiguration {

    private val passwordEncoder = createDelegatingPasswordEncoder()

    @Bean
    @Order(101)
    fun apiSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            securityMatcher("/api/**")

            defaults()

            httpBasic {}
            authorizeRequests {
                authorize("/api/**", hasAuthority(SCOPE_API))
            }
        }
        return http.build()
    }

    @Bean
    @Order(102)
    fun actuatorSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            securityMatcher(EndpointRequest.toAnyEndpoint())

            defaults()

            httpBasic {}
            authorizeRequests {
                authorize(EndpointRequest.to(InfoEndpoint::class.java), permitAll)
                authorize(EndpointRequest.toAnyEndpoint(), hasAuthority(SCOPE_ACTUATOR))
            }
        }
        return http.build()
    }

    @Bean
    @Order(103)
    fun graphiqlSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            securityMatcher("/graphiql")

            defaults()

            httpBasic {}
            authorizeRequests {
                authorize("/graphiql", hasAuthority(SCOPE_GRAPHIQL))
            }
        }
        return http.build()
    }

    @Bean
    @Order(199)
    fun generalSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            securityMatcher("/**")

            defaults()

            authorizeRequests {
                authorize("/docs/**", permitAll)
                authorize("/error", permitAll)
                authorize("/**", denyAll)
            }
        }
        return http.build()
    }

    private fun HttpSecurityDsl.defaults() {
        cors { disable() }
        csrf { disable() }
        headers { cacheControl {} }
        sessionManagement { sessionCreationPolicy = STATELESS }
    }

    @Bean
    fun inMemoryUserDetailsManager(properties: UsersProperties): InMemoryUserDetailsManager {
        val users = properties.basicAuth.map { user ->
            User.builder()
                .username(user.username)
                .password(passwordEncoder.encode(user.password))
                .authorities(user.authorities.map(::SimpleGrantedAuthority))
                .build()
        }
        return InMemoryUserDetailsManager(users)
    }

}

package wsb.lisowski.savingswallet.infrastructure.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    val jwtFilter: JwtFilter,
) {
    @Bean
    fun filterChain(http: HttpSecurity) = http
        .csrf { it.disable() } // ← Turned OFF CSRF!.csrf(Customizer.withDefaults())
        .authorizeHttpRequests {
            it
                .requestMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
        }
        .sessionManagement {
            it
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
        .build()

    @Bean
    fun authManager(
        http: HttpSecurity,
        passwordEncoder: PasswordEncoder,
        userDetailsService: SpringSecurityUserDetailsService
    ) = http
        .getSharedObject(AuthenticationManagerBuilder::class.java)
        .userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder)
        .and()
        .build()

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}
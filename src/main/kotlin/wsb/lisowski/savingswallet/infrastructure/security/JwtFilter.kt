package wsb.lisowski.savingswallet.infrastructure.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

@Component
class JwtFilter(
    val jwtService: JwtService,
    val userDetailsService: SpringSecurityUserDetailsService,
) : OncePerRequestFilter() {

    companion object {
        const val AUTH_HEADER = "Authorization"
        const val BEARER = "Bearer"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader(AUTH_HEADER);
        if (Objects.nonNull(authHeader) && authHeader.startsWith(BEARER)) {
            val jwt = authHeader.substring(7)
            val username = jwtService.extractUsername(jwt)

            if (username != null && SecurityContextHolder.getContext().authentication == null) {
                val userDetails = userDetailsService.loadUserByUsername(username)
                if (jwtService.validateToken(jwt, userDetails)) {
                    val authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    SecurityContextHolder.getContext().authentication = authToken;
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
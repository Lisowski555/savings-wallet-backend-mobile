package wsb.lisowski.savingswallet.infrastructure.security

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import wsb.lisowski.savingswallet.application.ports.UserRepo
import wsb.lisowski.savingswallet.infrastructure.security.SpringSecurityUserDetailsService.Companion.AUTHENTICATED_ROLE

@Component
class SpringSecurityUserDetailsService(
    val userRepo: UserRepo,
) : UserDetailsService {

    companion object {
        const val AUTHENTICATED_ROLE = "AUTHENTICATED"
    }

    override fun loadUserByUsername(username: String) = userRepo.findUserByUsername(username)
        ?.toSecurity("")
        ?: throw UsernameNotFoundException("User not found")
}

fun wsb.lisowski.savingswallet.domain.User.toSecurity(password: String) = User.withUsername(username)
    .password(password)
    .roles(AUTHENTICATED_ROLE)
    .build()
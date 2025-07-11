//package wsb.lisowski.savingswallet.application.controllers
//
//import org.springframework.web.bind.annotation.RequestMapping
//import org.springframework.web.bind.annotation.RestController
//
//@RestController
//@RequestMapping("/auth")
//class SavingsWalletController {
//}

package wsb.lisowski.savingswallet.application.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import wsb.lisowski.savingswallet.infrastructure.security.JwtService

@RestController
@RequestMapping("/auth")
class SavingsWalletController(
    private val jwtService: JwtService
) {

    data class LoginRequest(val username: String, val password: String)
    data class LoginResponse(val token: String)

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        // Testowy użytkownik (na sztywno)
        val validUser = "test"
        val validPass = "test123"

        return if (request.username == validUser && request.password == validPass) {
            val token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User.withUsername(validUser)
                    .password(validPass)
                    .roles("AUTHENTICATED")
                    .build()
            )
            ResponseEntity.ok(LoginResponse(token))
        } else {
            ResponseEntity.status(401).build()
        }
    }
}
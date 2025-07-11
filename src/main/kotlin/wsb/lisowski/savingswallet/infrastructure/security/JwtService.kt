package wsb.lisowski.savingswallet.infrastructure.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import io.jsonwebtoken.security.Keys

@Component
class JwtService(
    val secret: String = "secret",
) {

    fun generateToken(userDetails: UserDetails) = Jwts.builder()
        .setSubject(userDetails.username)
        .setIssuedAt(Date())
        .signWith(SignatureAlgorithm.HS256, secret)
        .compact()

    fun extractUsername(token: String) = Jwts.parser()
        .setSigningKey(secret)
        .parseClaimsJws(token)
        .body
        .subject

    fun validateToken(token: String, userDetails: UserDetails) = extractUsername(token) == userDetails.username
}
package wsb.lisowski.savingswallet.infrastructure.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import wsb.lisowski.savingswallet.domain.Id.Companion.id
import wsb.lisowski.savingswallet.domain.User
import java.util.*

@Document("users")
data class UserEntity(
        @Id val id: String,
        val username: String,
        val password: String,
) {
    fun toDomain() = User(
            id = id(UUID.fromString(id)),
            username,
            password
    )
}

fun User.toEntity() = UserEntity(
        id = id.value.toString(),
        username,
        password
)
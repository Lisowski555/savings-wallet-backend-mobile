package wsb.lisowski.savingswallet.infrastructure.persistence

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import wsb.lisowski.savingswallet.application.ports.UserRepo
import wsb.lisowski.savingswallet.domain.User

@Repository
class MongoDbUserRepo(
    val springUserRepo: SpringMongoDbUserRepo
) : UserRepo {
    override fun findUserByUsername(username: String) = springUserRepo.findByUsername(username)
        ?.toDomain()

    override fun saveUser(user: User) = user.toEntity()
        .apply { springUserRepo.save(this) }
        .toDomain()
}

interface SpringMongoDbUserRepo : MongoRepository<UserEntity, String> {
    fun findByUsername(username: String): UserEntity?
}
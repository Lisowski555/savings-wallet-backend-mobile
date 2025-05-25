package wsb.lisowski.savingswallet.infrastructure.persistence

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import wsb.lisowski.savingswallet.application.ports.SavingsWalletRepo
import wsb.lisowski.savingswallet.domain.Id
import wsb.lisowski.savingswallet.domain.SavingsWallet
import wsb.lisowski.savingswallet.domain.User
import java.time.Clock

@Repository
class MongoDbSavingsWalletRepo(
        val clock: Clock,
        val springSavingsWalletRepo: SpringMongoDbSavingsWalletRepo,
) : SavingsWalletRepo {
    override fun findSavingsWalletByUserId(userId: Id<User>) = springSavingsWalletRepo
            .findByUserId(userId.id.toString())
            ?.toDomain(clock)

    override fun saveSavingsWallet(savingsWallet: SavingsWallet) = savingsWallet.toEntity()
            .apply { springSavingsWalletRepo.save(this) }
            .toDomain(clock)
}

interface SpringMongoDbSavingsWalletRepo : MongoRepository<SavingsWalletEntity, String> {
    fun findByUserId(userId: String): SavingsWalletEntity?
}
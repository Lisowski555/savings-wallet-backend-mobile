package wsb.lisowski.savingswallet.infrastructure.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import wsb.lisowski.savingswallet.domain.*
import wsb.lisowski.savingswallet.domain.Currency
import wsb.lisowski.savingswallet.domain.Id.Companion.id
import java.math.BigDecimal
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Document
data class SavingsWalletEntity(
        @Id val id: String,
        val userId: String,
        val savingsAccounts: List<SavingsAccountEntity>,
        val savingsDeposits: List<SavingsDepositEntity>,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
) {
    fun toDomain(clock: Clock) = SavingsWallet(
            id = id<SavingsWallet>(UUID.fromString(id)),
            userId = id(UUID.fromString(userId)),
            savingsAccounts = savingsAccounts.map { it.toDomain() },
            savingsDeposits = savingsDeposits.map { it.toDomain() },
            created = createdAt,
            updated = updatedAt,
            clock,
    )
}

fun SavingsWallet.toEntity() = SavingsWalletEntity(
        id = id.value.toString(),
        userId = userId.value.toString(),
        savingsAccounts = savingsAccounts.map { it.toEntity() },
        savingsDeposits = savingsDeposits.map { it.toEntity() },
        createdAt = created,
        updatedAt = updated,
)

data class SavingsAccountEntity(
        @Id val id: String,
        val title: String,
        val amount: BigDecimal,
        val currency: Currency,
        val rate: BigDecimal,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
) {
    fun toDomain(): SavingsAccount = SavingsAccount(
            id = id<SavingsAccount>(UUID.fromString(id)),
            title = title,
            amount = Money(amount, currency),
            rate = rate,
            created = createdAt,
            updated = updatedAt,
    )
}

fun SavingsAccount.toEntity() = SavingsAccountEntity(
        id = id.value.toString(),
        title = title,
        amount = amount.amount,
        currency = amount.currency,
        rate = rate,
        createdAt = created,
        updatedAt = updated
)

data class SavingsDepositEntity(
        @Id val id: String,
        val amount: BigDecimal,
        val currency: Currency,
        val rate: BigDecimal,
        val endDate: LocalDate,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
) {
    fun toDomain(): SavingsDeposit = SavingsDeposit(
            id = id(UUID.fromString(id)),
            amount = Money(amount, currency),
            rate = rate,
            endDate = endDate,
            created = createdAt,
            updated = updatedAt,
    )
}

fun SavingsDeposit.toEntity() = SavingsDepositEntity(
        id = id.toString(),
        amount = amount.amount,
        currency = amount.currency,
        rate = rate,
        endDate = endDate,
        createdAt = created,
        updatedAt = updated,
)
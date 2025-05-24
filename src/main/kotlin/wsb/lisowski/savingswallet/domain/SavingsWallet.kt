package wsb.lisowski.savingswallet.domain

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class SavingsWallet(
    val id: Id<SavingsWallet>,
    val userId: UserId,
    val savingsAccounts: List<SavingsAccount>,
    val savingsDeposits: List<SavingsDeposit>,
    val version: Long,
    val created: LocalDateTime,
    val updated: LocalDateTime,
)

data class SavingsAccount(
    val id: Id<SavingsAccount>,
    val amount: Money,
    val rate: BigDecimal,
    val created: LocalDateTime,
    val updated: LocalDateTime,
)

data class SavingsDeposit(
    val id: Id<SavingsDeposit>,
    val amount: Money,
    val rate: BigDecimal,
    val endDate: LocalDate,
    val created: LocalDateTime,
    val updated: LocalDateTime,
)
package wsb.lisowski.savingswallet.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.Clock
import java.time.LocalDateTime
import java.time.LocalDateTime.now

data class SavingsWallet(
    val id: Id<SavingsWallet>,
    val userId: Id<User>,
    val savingsAccounts: List<SavingsAccount>,
    val savingsDeposits: List<SavingsDeposit>,
    val created: LocalDateTime,
    val updated: LocalDateTime,
    @JsonIgnore val clock: Clock,
) {
    fun createSavingsAccount(savingsAccount: SavingsAccount): SavingsWallet {
        return copy(
            savingsAccounts = savingsAccounts + savingsAccount,
            updated = now(clock)
        )
    }

    fun createSavingsDeposit(savingsDeposit: SavingsDeposit): SavingsWallet {
        return copy(
            savingsDeposits = savingsDeposits + savingsDeposit,
            updated = now(clock)
        )
    }
}
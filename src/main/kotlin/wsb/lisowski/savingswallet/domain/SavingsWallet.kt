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

    fun updateSavingsAccount(updated: SavingsAccount): SavingsWallet {
        val newAccounts = savingsAccounts.map {
            if (it.id == updated.id) updated else it
        }
        return copy(
            savingsAccounts = newAccounts,
            updated = now(clock)
        )
    }

    // Deleting account by ID
    fun removeSavingsAccount(accountId: Id<SavingsAccount>): SavingsWallet {
        return copy(
            savingsAccounts = savingsAccounts.filter { it.id != accountId },
            updated = now(clock)
        )
    }

    // Editing deposit
    fun updateSavingsDeposit(updated: SavingsDeposit): SavingsWallet {
        val newDeposits = savingsDeposits.map {
            if (it.id == updated.id) updated else it
        }
        return copy(
            savingsDeposits = newDeposits,
            updated = now(clock)
        )
    }

    // Deleting deposit by ID
    fun deleteSavingsDeposit(depositId: Id<SavingsDeposit>): SavingsWallet {
        val newDeposits = savingsDeposits.filter { it.id != depositId }
        return copy(
            savingsDeposits = newDeposits,
            updated = now(clock)
        )
    }
}
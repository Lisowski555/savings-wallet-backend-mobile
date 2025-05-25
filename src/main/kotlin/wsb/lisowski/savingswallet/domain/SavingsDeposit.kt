package wsb.lisowski.savingswallet.domain

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class SavingsDeposit(
        val id: Id<SavingsDeposit>,
        val amount: Money,
        val rate: BigDecimal,
        val endDate: LocalDate,
        val created: LocalDateTime,
        val updated: LocalDateTime,
)
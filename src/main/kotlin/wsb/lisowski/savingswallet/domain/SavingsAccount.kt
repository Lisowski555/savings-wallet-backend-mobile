package wsb.lisowski.savingswallet.domain

import java.math.BigDecimal
import java.time.LocalDateTime

data class SavingsAccount(
    val id: Id<SavingsAccount>,
    val title: String,
    val amount: Money,
    val rate: BigDecimal,
    val created: LocalDateTime,
    val updated: LocalDateTime,
)
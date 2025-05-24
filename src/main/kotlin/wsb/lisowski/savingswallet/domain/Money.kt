package wsb.lisowski.savingswallet.domain

import java.math.BigDecimal

data class Money(
    val amount: BigDecimal,
    val currency: Currency
)

enum class Currency {
    PLN,
    USD,
    EUR,
}

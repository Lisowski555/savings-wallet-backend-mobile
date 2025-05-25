package wsb.lisowski.savingswallet.domain

import java.math.BigDecimal

data class Money(
    val amount: BigDecimal,
    val currency: Currency
) {
    operator fun plus(other: Money): Money {
        require(currency == other.currency) { "Cannot sum money of different currencies" }
        return Money(amount + other.amount, currency)
    }

    operator fun minus(other: Money): Money {
        require(currency == other.currency) { "Cannot subtract money of different currencies" }
        return Money(amount - other.amount, currency)
    }

    operator fun times(multiplier: BigDecimal): Money = Money(amount * multiplier, currency)
    operator fun BigDecimal.times(money: Money): Money = money * this
}

enum class Currency {
    PLN,
    USD,
    EUR,
}

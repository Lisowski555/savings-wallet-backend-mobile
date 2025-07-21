package wsb.lisowski.savingswallet.application.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import wsb.lisowski.savingswallet.application.ports.SavingsWalletRepo
import wsb.lisowski.savingswallet.application.ports.UserRepo
import wsb.lisowski.savingswallet.domain.*
import wsb.lisowski.savingswallet.infrastructure.security.JwtService
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Clock

@RestController
@RequestMapping("/wallet")
class WalletController(
    private val walletRepo: SavingsWalletRepo,
    private val userRepo: UserRepo,
    private val clock: Clock,
    private val jwtService: JwtService
) {

    data class AccountDto(
        val title: String,
        val rate: Double,
        val amount: Double,
        val currency: Currency = Currency.PLN // domyślnie PLN
    )

    data class DepositDto(
        val title: String,
        val endDate: String,
        val rate: Double,
        val amount: Double,
        val currency: Currency = Currency.PLN
    )

    /** Pobierz portfel użytkownika (na razie na sztywno test/test123) */
    @GetMapping
    fun getWallet(@RequestHeader("Authorization") authHeader: String): ResponseEntity<SavingsWalletResponse> {
        val username = extractUsername(authHeader)
        val user = userRepo.findUserByUsername(username)
            ?: return ResponseEntity.status(404).build()
        val wallet = walletRepo.findSavingsWalletByUserId(user.id)
            ?: SavingsWallet(
                id = Id.randomId(),
                userId = user.id,
                savingsAccounts = emptyList(),
                savingsDeposits = emptyList(),
                created = LocalDateTime.now(clock),
                updated = LocalDateTime.now(clock),
                clock = clock
            ).let { walletRepo.saveSavingsWallet(it) }
        return ResponseEntity.ok(wallet.toResponse())
    }

    /** Dodaj nowe konto oszczędnościowe do portfela */
    @PostMapping("/accounts")
    fun addAccount(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody dto: AccountDto
    ): ResponseEntity<SavingsWalletResponse> {
        val username = extractUsername(authHeader)
        val user = userRepo.findUserByUsername(username)
            ?: return ResponseEntity.status(404).build()
        val wallet = walletRepo.findSavingsWalletByUserId(user.id)
            ?: return ResponseEntity.status(404).build()

        val newAccount = SavingsAccount(
            id = Id.randomId(),
            title = dto.title,
            amount = Money(BigDecimal(dto.amount), dto.currency),
            rate = BigDecimal(dto.rate),
            created = LocalDateTime.now(clock),
            updated = LocalDateTime.now(clock)
        )
        val updatedWallet = wallet.createSavingsAccount(newAccount)
        walletRepo.saveSavingsWallet(updatedWallet)
        return ResponseEntity.ok(updatedWallet.toResponse())
    }

    /** Dodaj nową lokatę do portfela */
    @PostMapping("/deposits")
    fun addDeposit(
        @RequestHeader("Authorization") authHeader: String,
        @RequestBody dto: DepositDto
    ): ResponseEntity<SavingsWalletResponse> {
        val username = extractUsername(authHeader)
        val user = userRepo.findUserByUsername(username)
            ?: return ResponseEntity.status(404).build()
        val wallet = walletRepo.findSavingsWalletByUserId(user.id)
            ?: return ResponseEntity.status(404).build()

        val newDeposit = SavingsDeposit(
            id = Id.randomId(),
            title = dto.title,
            amount = Money(BigDecimal(dto.amount), dto.currency),
            rate = BigDecimal(dto.rate),
            endDate = LocalDate.parse(dto.endDate),
            created = LocalDateTime.now(clock),
            updated = LocalDateTime.now(clock)
        )
        val updatedWallet = wallet.createSavingsDeposit(newDeposit)
        walletRepo.saveSavingsWallet(updatedWallet)
        return ResponseEntity.ok(updatedWallet.toResponse())
    }

    /** Pobierz nazwę użytkownika z nagłówka Authorization: Bearer ... */
    private fun extractUsername(authHeader: String): String {
        val token = authHeader.removePrefix("Bearer").trim()
        return jwtService.extractUsername(token)
    }

    private fun SavingsWallet.toResponse() = SavingsWalletResponse(
        id = this.id.value.toString(),
        userId = this.userId.value.toString(),
        savingsAccounts = this.savingsAccounts.map { it.toResponse() },
        savingsDeposits = this.savingsDeposits.map { it.toResponse() },
    )

    private fun SavingsAccount.toResponse() = SavingsAccountResponse(
        id = this.id.value.toString(),
        title = title,
        amount = amount,
        rate = rate,
    )

    private fun SavingsDeposit.toResponse() = SavingsDepositResponse(
        id = this.id.value.toString(),
        title = title,
        amount = amount,
        rate = rate,
        endDate = endDate,
    )

    data class SavingsWalletResponse(
        val id: String,
        val userId: String,
        val savingsAccounts: List<SavingsAccountResponse>,
        val savingsDeposits: List<SavingsDepositResponse>,
    )

    data class SavingsAccountResponse(
        val id: String,
        val title: String,
        val amount: Money,
        val rate: BigDecimal,
    )

    data class SavingsDepositResponse(
        val id: String,
        val title: String,
        val amount: Money,
        val rate: BigDecimal,
        val endDate: LocalDate,
    )
}
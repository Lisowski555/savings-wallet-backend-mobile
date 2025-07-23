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

    @PutMapping("/accounts/{id}")
    fun updateAccount(
        @RequestHeader("Authorization") authHeader: String,
        @PathVariable id: String,
        @RequestBody dto: AccountDto
    ): ResponseEntity<SavingsWalletResponse> {
        val username = extractUsername(authHeader)
        val user = userRepo.findUserByUsername(username)
            ?: return ResponseEntity.status(404).build()
        val wallet = walletRepo.findSavingsWalletByUserId(user.id)
            ?: return ResponseEntity.status(404).build()
        val accountId = Id.id<SavingsAccount>(java.util.UUID.fromString(id))
        val existing = wallet.savingsAccounts.find { it.id == accountId }
            ?: return ResponseEntity.status(404).build()
        val updatedAccount = existing.copy(
            title = dto.title,
            amount = Money(BigDecimal(dto.amount), dto.currency),
            rate = BigDecimal(dto.rate),
            updated = LocalDateTime.now(clock)
        )
        val updatedWallet = wallet.updateSavingsAccount(updatedAccount)
        walletRepo.saveSavingsWallet(updatedWallet)
        return ResponseEntity.ok(updatedWallet.toResponse())
    }

    // ENDPOINT: DELETING savings accounts
    @DeleteMapping("/accounts/{id}")
    fun deleteAccount(
        @RequestHeader("Authorization") authHeader: String,
        @PathVariable id: String
    ): ResponseEntity<SavingsWalletResponse> {
        val username = extractUsername(authHeader)
        val user = userRepo.findUserByUsername(username)
            ?: return ResponseEntity.status(404).build()
        val wallet = walletRepo.findSavingsWalletByUserId(user.id)
            ?: return ResponseEntity.status(404).build()
        val accountId = Id.id<SavingsAccount>(java.util.UUID.fromString(id))
        val exists = wallet.savingsAccounts.any { it.id == accountId }
        if (!exists) return ResponseEntity.status(404).build()
        val updatedWallet = wallet.removeSavingsAccount(accountId)
        walletRepo.saveSavingsWallet(updatedWallet)
        return ResponseEntity.ok(updatedWallet.toResponse())
    }

    // Editing and deleting savings deposits

    @PutMapping("/deposits/{id}")
    fun updateDeposit(
        @RequestHeader("Authorization") authHeader: String,
        @PathVariable id: String,
        @RequestBody dto: DepositDto
    ): ResponseEntity<SavingsWalletResponse> {
        val username = extractUsername(authHeader)
        val user = userRepo.findUserByUsername(username)
            ?: return ResponseEntity.status(404).build()
        val wallet = walletRepo.findSavingsWalletByUserId(user.id)
            ?: return ResponseEntity.status(404).build()
        val depositId = Id.id<SavingsDeposit>(java.util.UUID.fromString(id))
        val existing = wallet.savingsDeposits.find { it.id == depositId }
            ?: return ResponseEntity.status(404).build()
        val updatedDeposit = existing.copy(
            title = dto.title,
            amount = Money(BigDecimal(dto.amount), dto.currency),
            rate = BigDecimal(dto.rate),
            endDate = LocalDate.parse(dto.endDate),
            updated = LocalDateTime.now(clock)
        )
        val updatedWallet = wallet.updateSavingsDeposit(updatedDeposit)
        walletRepo.saveSavingsWallet(updatedWallet)
        return ResponseEntity.ok(updatedWallet.toResponse())
    }

    @DeleteMapping("/deposits/{id}")
    fun deleteDeposit(
        @RequestHeader("Authorization") authHeader: String,
        @PathVariable id: String
    ): ResponseEntity<SavingsWalletResponse> {
        val username = extractUsername(authHeader)
        val user = userRepo.findUserByUsername(username)
            ?: return ResponseEntity.status(404).build()
        val wallet = walletRepo.findSavingsWalletByUserId(user.id)
            ?: return ResponseEntity.status(404).build()
        val depositId = Id.id<SavingsDeposit>(java.util.UUID.fromString(id))
        val updatedWallet = wallet.deleteSavingsDeposit(depositId)
        walletRepo.saveSavingsWallet(updatedWallet)
        return ResponseEntity.ok(updatedWallet.toResponse())
    }

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
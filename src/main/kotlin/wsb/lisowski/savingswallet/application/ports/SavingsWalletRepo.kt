package wsb.lisowski.savingswallet.application.ports

import wsb.lisowski.savingswallet.domain.Id
import wsb.lisowski.savingswallet.domain.SavingsWallet
import wsb.lisowski.savingswallet.domain.User

interface SavingsWalletRepo {
    fun findSavingsWalletByUserId(userId: Id<User>): SavingsWallet?
    fun saveSavingsWallet(savingsWallet: SavingsWallet): SavingsWallet
}
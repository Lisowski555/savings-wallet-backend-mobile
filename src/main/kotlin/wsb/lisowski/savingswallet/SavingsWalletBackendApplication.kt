package wsb.lisowski.savingswallet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SavingsWalletBackendApplication

fun main(args: Array<String>) {
    runApplication<SavingsWalletBackendApplication>(*args)
}

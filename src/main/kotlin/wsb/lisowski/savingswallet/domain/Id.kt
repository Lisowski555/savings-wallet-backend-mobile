package wsb.lisowski.savingswallet.domain

import java.util.*
import java.util.UUID.randomUUID

data class Id<T>(val id: UUID, val type: Class<T>) {
    companion object {
        inline fun <reified T> randomId() = Id(randomUUID(), T::class.java)
        inline fun <reified T> id(uuid: UUID) = Id(uuid, T::class.java)
    }
}


package wsb.lisowski.savingswallet.domain

import wsb.lisowski.savingswallet.domain.Id.Companion.id
import wsb.lisowski.savingswallet.domain.Id.Companion.randomId
import kotlin.test.Test

class IdTest {

    @Test
    fun `different classes with same uuids does not equal`() {
        // given
        val id1 = randomId<SavingsDeposit>()
        val id2 = id<SavingsAccount>(id1.value)

        // when / then
        assert(id1 != id2) { "Id should be different for different classes" }
    }

    @Test
    fun `same classes with the same uuids equal`() {
        // given
        val id1 = randomId<SavingsDeposit>()
        val id2 = id<SavingsDeposit>(id1.value)

        // when / then
        assert(id1 == id2) { "Id should be the same for the same classes" }
    }

    @Test
    fun `same classes with random uuids does not equal`() {
        // given
        val id1 = randomId<SavingsDeposit>()
        val id2 = randomId<SavingsDeposit>()

        // when / then
        assert(id1 != id2) { "Id should be the same for the same classes" }
    }
}

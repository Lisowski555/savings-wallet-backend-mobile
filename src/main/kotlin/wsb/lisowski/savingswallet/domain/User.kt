package wsb.lisowski.savingswallet.domain

data class User(
        val id: Id<User>,
        val username: String,
        val password: String
) {
}
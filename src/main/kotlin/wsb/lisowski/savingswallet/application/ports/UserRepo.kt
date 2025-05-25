package wsb.lisowski.savingswallet.application.ports

import wsb.lisowski.savingswallet.domain.User

interface UserRepo {
    fun findUserByUsername(username: String): User?
    fun saveUser(user: User): User
}
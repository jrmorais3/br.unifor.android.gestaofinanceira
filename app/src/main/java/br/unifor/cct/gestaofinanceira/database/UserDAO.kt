package br.unifor.cct.gestaofinanceira.database

import androidx.room.*
import br.unifor.cct.gestaofinanceira.model.User
import br.unifor.cct.gestaofinanceira.model.UserWithLancamentos

@Dao
interface UserDAO {
    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM users WHERE uid = :uid")
    fun find(uid: Int): User

    @Query("SELECT * FROM users WHERE email = :email")
    fun findByEmail(email: String): User

    @Query("SELECT * FROM users")
    fun findAll():List<User>

    @Transaction
    @Query("SELECT * FROM users WHERE uid = :uid")
    fun findTasksByUserId(uid: Int): UserWithLancamentos
}
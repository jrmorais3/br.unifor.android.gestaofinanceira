package br.unifor.cct.gestaofinanceira.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.unifor.cct.gestaofinanceira.model.Lancamento
import br.unifor.cct.gestaofinanceira.model.User

@Database(entities = [User::class, Lancamento::class], version = 1)
abstract class GFDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDAO

    abstract fun getTaskDAO(): LancamentoDAO
}
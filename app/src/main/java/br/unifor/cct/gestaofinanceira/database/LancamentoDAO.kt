package br.unifor.cct.gestaofinanceira.database

import androidx.room.*
import br.unifor.cct.gestaofinanceira.model.Lancamento

@Dao
interface LancamentoDAO {
    @Insert
    fun insert(lancamento: Lancamento)

    @Update
    fun update(lancamento: Lancamento)

    @Delete
    fun delete(lancamento: Lancamento)

    @Query("SELECT * FROM lancamento WHERE tid = :tid")
    fun find(tid: Int): Lancamento

    @Query("SELECT * FROM lancamento")
    fun findAll():List<Lancamento>
}
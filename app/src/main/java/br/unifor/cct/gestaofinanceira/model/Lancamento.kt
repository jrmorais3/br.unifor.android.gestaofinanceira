package br.unifor.cct.gestaofinanceira.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lancamento")
data class Lancamento(
    @PrimaryKey
    val tid:Int? = null,
    val name:String,
    val value:String,
    @ColumnInfo(name="is_done")
    val isReceita:Boolean,
    @ColumnInfo(name="user_id")
    val userId:Int
)
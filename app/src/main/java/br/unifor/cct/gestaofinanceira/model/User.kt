package br.unifor.cct.gestaofinanceira.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName ="users", indices = [
    Index(value = ["email"], unique = true)
])
data class User (
    @PrimaryKey
    val uid: Int? = null,

    @ColumnInfo(name = "first_name")
    val firstName: String,

    @ColumnInfo(name = "last_name")
    val lastName: String,
    val email: String,
    val password: String,
)
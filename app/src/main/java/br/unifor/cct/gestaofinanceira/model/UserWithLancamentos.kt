package br.unifor.cct.gestaofinanceira.model

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithLancamentos (
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "uid",
        entityColumn = "user_id"
    )
    val lancamentos: List<Lancamento>,
)
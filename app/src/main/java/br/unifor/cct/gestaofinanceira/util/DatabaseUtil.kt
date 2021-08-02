package br.unifor.cct.gestaofinanceira.util

import android.content.Context
import androidx.room.Room
import br.unifor.cct.gestaofinanceira.database.GFDatabase

object DatabaseUtil {
    private var db: GFDatabase ? = null

    fun getInstance(context: Context): GFDatabase {
        if (this.db == null) {
            this.db = Room.databaseBuilder(
                context,
                GFDatabase::class.java,
                "gestaofinanceira.db"
            ).fallbackToDestructiveMigration().build()
        }
        return db!!
    }
}
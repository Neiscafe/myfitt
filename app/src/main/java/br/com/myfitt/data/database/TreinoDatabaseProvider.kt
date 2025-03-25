package br.com.myfitt.data.database

import android.content.Context
import androidx.room.Room

object TreinoDatabaseProvider {

    fun getDatabase(context: Context): TreinoDatabase {
        return synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                TreinoDatabase::class.java,
                "treino_database"
            ).build()
            instance
        }
    }
}

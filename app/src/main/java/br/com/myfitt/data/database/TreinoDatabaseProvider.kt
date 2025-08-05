package br.com.myfitt.data.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object TreinoDatabaseProvider {
    /**
     * Remover: DIVISAO e PLANILHA
     * Quem tem DIVISAO: FICHA
     * Quem tem planilha: TREINO
     *
     */

    private val gruposMusculares = listOf(
        "Peito",
        "Quadríceps",
        "Posterior de Coxa",
        "Glúteo",
        "Ombro",
        "Tríceps",
        "Bíceps",
        "Costas (Largura)",
        "Costas (Espessura)",
        "Panturrilha",
        "Abdômen",
        "Trapézio",
        "Antebraço",
        "Lombar",
        "Adutores",
        "Abdutores"
    )

    fun getDatabase(context: Context): TreinoDatabase {
        val instance = Room.databaseBuilder(
            context.applicationContext, TreinoDatabase::class.java, "treino_database_2"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                rawQuery(db) {
                    gruposMusculares.forEach { nome ->
                        val values = ContentValues().apply {
                            put("nome", nome)
                        }
                        insert(
                            "exercicio_tipo", SQLiteDatabase.CONFLICT_IGNORE, values
                        )
                    }
                }
            }

            override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                super.onDestructiveMigration(db)
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
            }
        }).build()
        return instance
    }

    private fun rawQuery(db: SupportSQLiteDatabase, action: SupportSQLiteDatabase.() -> Unit) {
        db.beginTransaction()
        try {
            action(db)
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }
}

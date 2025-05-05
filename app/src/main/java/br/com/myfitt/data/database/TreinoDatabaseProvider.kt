package br.com.myfitt.data.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object TreinoDatabaseProvider {
    private val MIGRATION_7_8 = object : Migration(7, 8) {
        override fun migrate(db: SupportSQLiteDatabase) {
//            db.beginTransaction()
            db.execSQL("DROP TABLE `divisao`;")
            db.execSQL("DROP TABLE `ficha`;")
            db.execSQL("DROP TABLE `ficha_exercicio`;")

            db.execSQL("CREATE TABLE IF NOT EXISTS `divisao` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nome` TEXT NOT NULL);")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_divisao_id` ON `divisao` (`id`);")

            db.execSQL("CREATE TABLE IF NOT EXISTS `ficha` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `divisaoId` INTEGER NOT NULL, `nome` TEXT NOT NULL, FOREIGN KEY(`divisaoId`) REFERENCES `divisao`(`id`));")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_ficha_id` ON `ficha` (`id`);")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_ficha_divisaoId` ON `ficha` (`divisaoId`);")

            db.execSQL("CREATE TABLE IF NOT EXISTS `ficha_exercicio` (`fichaId` INTEGER NOT NULL, `exercicioId` INTEGER NOT NULL, `position` INTEGER NOT NULL DEFAULT 0, PRIMARY KEY(`fichaId`, `exercicioId`), FOREIGN KEY (`fichaId`) REFERENCES `ficha` (`id`), FOREIGN KEY(`exercicioId`) REFERENCES `exercicios`(`id`));")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_ficha_exercicio_fichaId` ON `ficha_exercicio` (`fichaId`);")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_ficha_exercicio_exercicioId` ON `ficha_exercicio` (`exercicioId`);")
//            db.endTransaction()
        }

    }
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
            context.applicationContext, TreinoDatabase::class.java, "treino_database"
        ).addMigrations(MIGRATION_7_8).addCallback(object : RoomDatabase.Callback() {
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

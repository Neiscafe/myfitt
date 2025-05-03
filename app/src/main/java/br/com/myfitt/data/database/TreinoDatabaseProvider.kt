package br.com.myfitt.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object TreinoDatabaseProvider {
    private val MIGRATION_7_8 = object : Migration(7, 8) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.beginTransaction()
            db.execSQL("DROP TABLE `divisao`;")
            db.execSQL("DROP TABLE `ficha`;")
            db.execSQL("DROP TABLE `ficha_exercicio`;")
//            db.execSQL("DROP INDEX `index_ficha_exercicio_fichaId`;")
//            db.execSQL("DROP INDEX `index_ficha_exercicio_exercicioId`;")
//            db.execSQL("DROP INDEX `index_ficha_id`;")
//            db.execSQL("DROP INDEX `index_ficha_divisaoId`;")
//            db.execSQL("DROP INDEX `index_divisao_id`;")

            db.execSQL("CREATE TABLE IF NOT EXISTS `divisao` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `nome` TEXT NOT NULL);")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_divisao_id` ON `divisao` (`id`);")

            db.execSQL("CREATE TABLE IF NOT EXISTS `ficha` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `divisaoId` INTEGER NOT NULL, `nome` TEXT NOT NULL, FOREIGN KEY(`divisaoId`) REFERENCES `divisao`(`id`));")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_ficha_id` ON `ficha` (`id`);")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_ficha_divisaoId` ON `ficha` (`divisaoId`);")

            db.execSQL("CREATE TABLE IF NOT EXISTS `ficha_exercicio` (`fichaId` INTEGER NOT NULL, `exercicioId` INTEGER NOT NULL, `position` INTEGER NOT NULL DEFAULT 0, PRIMARY KEY(`fichaId`, `exercicioId`), FOREIGN KEY (`fichaId`) REFERENCES `ficha` (`id`), FOREIGN KEY(`exercicioId`) REFERENCES `exercicios`(`id`));")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_ficha_exercicio_fichaId` ON `ficha_exercicio` (`fichaId`);")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_ficha_exercicio_exercicioId` ON `ficha_exercicio` (`exercicioId`);")
            db.endTransaction()
        }

    }

    fun getDatabase(context: Context): TreinoDatabase {
        return synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext, TreinoDatabase::class.java, "treino_database"
            ).addMigrations(MIGRATION_7_8).build()
            instance
        }
    }
}

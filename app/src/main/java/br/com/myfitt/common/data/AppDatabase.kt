package br.com.myfitt.common.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import br.com.myfitt.treinos.data.converters.Converters
import br.com.myfitt.treinos.data.dao.ExercicioDao
import br.com.myfitt.treinos.data.dao.ExercicioTreinoDao
import br.com.myfitt.treinos.data.dao.SerieExercicioDao
import br.com.myfitt.treinos.data.dao.TreinoDao
import br.com.myfitt.treinos.data.entities.ExercicioEntity
import br.com.myfitt.treinos.data.entities.ExercicioTreinoEntity
import br.com.myfitt.treinos.data.entities.SerieExercicioEntity
import br.com.myfitt.treinos.data.entities.TipoExercicioEntity
import br.com.myfitt.treinos.data.entities.TreinoEntity

@Database(
    entities = [TreinoEntity::class, ExercicioTreinoEntity::class, SerieExercicioEntity::class, ExercicioEntity::class, TipoExercicioEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun treinoDao(): TreinoDao
    abstract fun exercicioDao(): ExercicioDao
    abstract fun serieExercicioDao(): SerieExercicioDao
    abstract fun exercicioTreinoDao(): ExercicioTreinoDao
    fun build(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "AppDatabase.db")
            .fallbackToDestructiveMigration().addCallback(PopulaOnCreate).build()
    }

    object PopulaOnCreate : Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            db.beginTransaction()
            try {
                val tiposExercicio = listOf(
                    "Quadriceps",
                    "Posterior de coxa",
                    "Glúteo",
                    "Panturrilhas",
                    "Adutores",
                    "Peito",
                    "Ombro",
                    "Tríceps",
                    "Bíceps",
                    "Antebraço",
                    "Abdômen",
                    "Trapézio",
                    "Dorsal",
                    "Lombar"
                )

                tiposExercicio.forEach { descricao ->
                    db.execSQL("INSERT INTO tipos_exercicio (descricao) VALUES ('$descricao')")
                }

                val tiposTreino = listOf(
                    "Corpo completo",
                    "Superiores completo",
                    "Inferiores completo",
                    "Empurrar",
                    "Puxar",
                    "Braços",
                    "Ombros",
                    "Braços+Ombros",
                    "Inferiores (anterior)",
                    "Inferiores (posterior)",
                    "Glúteos+Coxas"
                )

                tiposTreino.forEach { descricao ->
                    db.execSQL("INSERT INTO tipos_treino (descricao) VALUES ('$descricao')")
                }

                db.setTransactionSuccessful()
            } finally {
                db.endTransaction()
            }
        }
    }
}
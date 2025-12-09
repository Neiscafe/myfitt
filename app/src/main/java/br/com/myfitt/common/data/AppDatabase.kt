package br.com.myfitt.common.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.myfitt.treinos.data.converters.Converters
import br.com.myfitt.treinos.data.dao.ExercicioDao
import br.com.myfitt.treinos.data.dao.ExercicioTreinoDao
import br.com.myfitt.treinos.data.dao.SerieExercicioDao
import br.com.myfitt.treinos.data.dao.TreinoDao
import br.com.myfitt.treinos.data.entities.ExercicioEntity
import br.com.myfitt.treinos.data.entities.ExercicioTreinoEntity
import br.com.myfitt.treinos.data.entities.SerieExercicioEntity
import br.com.myfitt.treinos.data.entities.TreinoEntity

@Database(
    entities = [TreinoEntity::class, ExercicioTreinoEntity::class, SerieExercicioEntity::class, ExercicioEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun treinoDao(): TreinoDao
    abstract fun exercicioDao(): ExercicioDao
    abstract fun serieExercicioDao(): SerieExercicioDao
    abstract fun exercicioTreinoDao(): ExercicioTreinoDao
}
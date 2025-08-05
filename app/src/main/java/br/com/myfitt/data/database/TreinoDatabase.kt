package br.com.myfitt.data.database


import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.myfitt.data.dao.ExercicioDao
import br.com.myfitt.data.dao.FichaDao
import br.com.myfitt.data.dao.FichaExercicioDao
import br.com.myfitt.data.dao.TreinoDao
import br.com.myfitt.data.dao.TreinoExercicioDao
import br.com.myfitt.data.entity.ExercicioEntity
import br.com.myfitt.data.entity.ExercicioTipoEntity
import br.com.myfitt.data.entity.FichaEntity
import br.com.myfitt.data.entity.FichaExercicioEntity
import br.com.myfitt.data.entity.TreinoEntity
import br.com.myfitt.data.entity.TreinoExercicioEntity
import br.com.myfitt.data.entity.TreinoExercicioSerieEntity

@Database(
    entities = [TreinoEntity::class, ExercicioEntity::class, TreinoExercicioEntity::class, ExercicioTipoEntity::class, FichaExercicioEntity::class, FichaEntity::class, TreinoExercicioSerieEntity::class],
    version = 1,
    exportSchema = true
)
abstract class TreinoDatabase : RoomDatabase() {
    abstract fun treinoExercicioDao(): TreinoExercicioDao
    abstract fun treinoDao(): TreinoDao
    abstract fun exercicioDao(): ExercicioDao
    abstract fun fichaDao(): FichaDao
    abstract fun fichaExercicioDao(): FichaExercicioDao
}

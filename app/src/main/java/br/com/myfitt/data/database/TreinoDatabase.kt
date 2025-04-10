package br.com.myfitt.data.database


import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.myfitt.data.dao.ExercicioDao
import br.com.myfitt.data.dao.PlanilhaDao
import br.com.myfitt.data.dao.TreinoDao
import br.com.myfitt.data.dao.TreinoExercicioDao
import br.com.myfitt.data.entity.ExercicioEntity
import br.com.myfitt.data.entity.PlanilhaEntity
import br.com.myfitt.data.entity.TreinoEntity
import br.com.myfitt.data.entity.TreinoExercicioCrossRef

@Database(
    entities = [PlanilhaEntity::class, TreinoEntity::class, ExercicioEntity::class, TreinoExercicioCrossRef::class],
    version = 3,
    autoMigrations = [AutoMigration(1,2), AutoMigration(2,3)],
    exportSchema = true
)
abstract class TreinoDatabase : RoomDatabase() {
    abstract fun treinoExercicioDao(): TreinoExercicioDao
    abstract fun planilhaDao(): PlanilhaDao
    abstract fun treinoDao(): TreinoDao
    abstract fun exercicioDao(): ExercicioDao
}

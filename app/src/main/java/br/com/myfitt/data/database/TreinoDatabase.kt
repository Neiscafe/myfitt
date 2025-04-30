package br.com.myfitt.data.database


import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.myfitt.data.dao.DivisaoDao
import br.com.myfitt.data.dao.ExercicioDao
import br.com.myfitt.data.dao.PlanilhaDao
import br.com.myfitt.data.dao.FichaDao
import br.com.myfitt.data.dao.FichaExercicioDao
import br.com.myfitt.data.dao.TreinoDao
import br.com.myfitt.data.dao.TreinoExercicioDao
import br.com.myfitt.data.entity.ExercicioEntity
import br.com.myfitt.data.entity.ExercicioTipoEntity
import br.com.myfitt.data.entity.PlanilhaEntity
import br.com.myfitt.data.entity.DivisaoEntity
import br.com.myfitt.data.entity.FichaEntity
import br.com.myfitt.data.entity.FichaExercicioCrossRef
import br.com.myfitt.data.entity.TreinoEntity
import br.com.myfitt.data.entity.TreinoExercicioCrossRef

@Database(
    entities = [PlanilhaEntity::class, TreinoEntity::class, ExercicioEntity::class, TreinoExercicioCrossRef::class, ExercicioTipoEntity::class, DivisaoEntity::class, FichaExercicioCrossRef::class, FichaEntity::class],
    version = 5,
    autoMigrations = [AutoMigration(1, 2), AutoMigration(2, 3), AutoMigration(
        3, 4
    ), AutoMigration(4, 5, Specs_4_5::class) ],
    exportSchema = true
)
abstract class TreinoDatabase : RoomDatabase() {
    abstract fun treinoExercicioDao(): TreinoExercicioDao
    abstract fun planilhaDao(): PlanilhaDao
    abstract fun treinoDao(): TreinoDao
    abstract fun exercicioDao(): ExercicioDao
    abstract fun fichaDao(): FichaDao
    abstract fun divisaoDao(): DivisaoDao
    abstract fun fichaExercicioDao(): FichaExercicioDao
}

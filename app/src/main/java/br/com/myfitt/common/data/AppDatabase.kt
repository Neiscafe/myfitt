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
import br.com.myfitt.treinos.data.dao.TipoExercicioDao
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
    abstract fun tipoExercicioDao(): TipoExercicioDao
    abstract fun serieExercicioDao(): SerieExercicioDao
    abstract fun exercicioTreinoDao(): ExercicioTreinoDao

    companion object {
        fun build(context: Context): Builder<AppDatabase> {
//            .createFromAsset("AppDatabase.db")
            return Room.databaseBuilder(context, AppDatabase::class.java, "AppDatabase.db").addCallback(PopulaOnCreate)
        }
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

                db.execSQL("""
                    INSERT INTO exercicios (nome, tipoExercicioId) VALUES 
                    -- 1. QUADRICEPS
                    ('Agachamento', 1),
                    ('Agachamento Sumo', 1),
                    ('Cadeira Extensora', 1),
                    ('Afundo', 1),
                    ('Agachamento Hack', 1),

                    -- 2. POSTERIOR DE COXA
                    ('Mesa Flexora', 2),
                    ('Cadeira Flexora', 2),
                    ('Stiff com Barra', 2),

                    -- 3. GLÚTEO
                    ('Elevacao Pelvica', 3),
                    ('Gluteo na Polia (Cabo)', 3),
                    ('Cadeira Abdutora', 3),

                    -- 4. PANTURRILHAS
                    ('Panturrilha Sentado (Gêmeos)', 4),
                    ('Panturrilha no Leg Press', 4),
                    ('Panturrilha em Pé (Máquina)', 4),
                    ('Panturrilha Hack)', 4),

                    -- 5. ADUTORES
                    ('Cadeira Adutora', 5),

                    -- 6. PEITO
                    ('Supino Reto', 6),
                    ('Supino Reto H.', 6),
                    ('Supino Inclinado H.', 6),
                    ('Crucifixo', 6),
                    ('Voador', 6),

                    -- 7. OMBRO
                    ('Desenvolvimento H.', 7),
                    ('Desenvolvimento.', 7),
                    ('Elevacao Lateral', 7),
                    ('Elevacao Frontal', 7),
                    ('Crucifixo Inverso', 7),

                    -- 8. TRÍCEPS
                    ('Triceps Corda', 8),
                    ('Triceps Pulley', 8),
                    ('Triceps Testa ', 8),
                    ('Triceps Frances', 8),
                    ('Paralela', 8),
                    -- 9. BÍCEPS
                    ('Rosca Direta H.', 9),
                    ('Rosca Scott H.', 9),
                    ('Rosca Scott M.', 9),
                    ('Rosca Alternada', 9),
                    ('Rosca Martelo H.', 9),
                    ('Rosca Martelo M.', 9),

                    -- 10. ANTEBRAÇO
                    ('Rosca Inversa', 10),
                    ('Flexao de Punho', 10),

                    -- 11. ABDÔMEN
                    ('Abdominal Supra', 11),
                    ('Abdominal Infra', 11),
                    ('Prancha Isométrica', 11),

                    -- 12. TRAPÉZIO
                    ('Encolhimento com Halteres', 12),
                    ('Remada Alta', 12),
                    ('Remada Curvada', 12),

                    -- 13. DORSAL
                    ('Puxada Alta Aberta', 13),
                    ('Puxada Alta Fechada', 13),
                    ('Remada Baixa', 13),
                    ('Pulldown', 13),

                    -- 14. LOMBAR
                    ('Hiperextensão Lombar', 14);
                """)
                db.setTransactionSuccessful()
            } finally {
                db.endTransaction()
            }
        }
    }
}
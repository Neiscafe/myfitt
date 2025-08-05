package br.com.myfitt

import br.com.myfitt.data.database.TreinoDatabase
import br.com.myfitt.data.database.TreinoDatabaseProvider
import br.com.myfitt.data.repository.ExercicioRepository
import br.com.myfitt.data.repository.FichaRepository
import br.com.myfitt.data.repository.TreinoExercicioRepository
import br.com.myfitt.data.repository.TreinoRepository
import br.com.myfitt.ui.viewModels.ExerciciosFichaViewModel
import br.com.myfitt.ui.viewModels.ExerciciosTreinoViewModel
import br.com.myfitt.ui.viewModels.FichasDivisaoViewModel
import br.com.myfitt.ui.viewModels.TreinosPlanilhaViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {
    single<TreinoDatabase> { TreinoDatabaseProvider.getDatabase(androidApplication()) }
}
val daoModule = module {
    single { get<TreinoDatabase>().treinoDao() }
    single { get<TreinoDatabase>().treinoExercicioDao() }
    single { get<TreinoDatabase>().exercicioDao() }
    single { get<TreinoDatabase>().fichaDao() }
    single { get<TreinoDatabase>().fichaExercicioDao() }
}
val repositoryModule = module {
    single { TreinoExercicioRepository(get(), get()) }
    single { ExercicioRepository(get()) }
    single { TreinoRepository(get()) }
    single { FichaRepository(get(), get()) }
    single { ExerciciosFichaViewModel(it.get(), get(), get()) }
}
val viewModelModule = module {
    viewModel { ExerciciosTreinoViewModel(it.get(), get(), get(), get()) }
    viewModel { TreinosPlanilhaViewModel(get()) }
    viewModel { FichasDivisaoViewModel( get()) }
}
val appModule = listOf(repositoryModule, daoModule, databaseModule, viewModelModule)
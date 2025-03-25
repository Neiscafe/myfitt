package br.com.myfitt

import br.com.myfitt.data.database.TreinoDatabase
import br.com.myfitt.data.database.TreinoDatabaseProvider
import br.com.myfitt.data.repository.ExercicioRepository
import br.com.myfitt.data.repository.PlanilhaRepository
import br.com.myfitt.data.repository.TreinoExercicioRepository
import br.com.myfitt.data.repository.TreinoRepository
import br.com.myfitt.ui.viewModels.ExercicioViewModel
import br.com.myfitt.ui.viewModels.PlanilhaViewModel
import br.com.myfitt.ui.viewModels.TreinoViewModel
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
    single { get<TreinoDatabase>().planilhaDao() }
}
val repositoryModule = module {
    single { TreinoExercicioRepository(get(), get()) }
    single { ExercicioRepository(get()) }
    single { TreinoRepository(get()) }
    single { PlanilhaRepository(get()) }
}
val viewModelModule = module {
    viewModel { ExercicioViewModel(it.get(), get(), get()) }
    viewModel { PlanilhaViewModel(get()) }
    viewModel { TreinoViewModel(get()) }
}
val appModule = listOf(repositoryModule, daoModule, databaseModule, viewModelModule)
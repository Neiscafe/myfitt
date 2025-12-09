package br.com.myfitt

import br.com.myfitt.treinos.data.repository.ExercicioRepositoryImpl
import br.com.myfitt.treinos.data.repository.ExercicioTreinoRepositoryImpl
import br.com.myfitt.treinos.data.repository.SeriesRepositoryImpl
import br.com.myfitt.treinos.data.repository.TreinoRepositoryImpl
import br.com.myfitt.treinos.domain.facade.TreinoFacade
import br.com.myfitt.treinos.domain.repository.ExercicioRepository
import br.com.myfitt.treinos.domain.repository.ExercicioTreinoRepository
import br.com.myfitt.treinos.domain.repository.SeriesRepository
import br.com.myfitt.treinos.domain.repository.TreinoRepository
import br.com.myfitt.treinos.ui.CronometroFacade
import br.com.myfitt.treinos.ui.screens.exerciciosTreino.ExerciciosTreinoViewModel
import br.com.myfitt.treinos.ui.screens.listaExercicios.ListaExerciciosViewModel
import br.com.myfitt.treinos.ui.screens.menuPrincipal.MenuPrincipalViewModel
import br.com.myfitt.treinos.ui.screens.seriesExercicio.SeriesExercicioViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.core.scope.Scope
import org.koin.dsl.module

val databaseModule = module {

}
val daoModule = module {

}
val repositoryModule = module {
    single<ExercicioTreinoRepository> { ExercicioTreinoRepositoryImpl() }
    single<ExercicioRepository> { ExercicioRepositoryImpl() }
    single<TreinoRepository> { TreinoRepositoryImpl() }
    single<SeriesRepository> { SeriesRepositoryImpl() }
}
val facadeModule = module {
    single { CronometroFacade(application.appScope) }
    single { TreinoFacade(get(), get(), get()) }
}
val viewModelModule = module {
    viewModel { ExerciciosTreinoViewModel(it[0], get(), get(), get()) }
    viewModel { ListaExerciciosViewModel(get()) }
    viewModel { MenuPrincipalViewModel(get()) }
    viewModel { SeriesExercicioViewModel(it[0], get(), get(), get(), get()) }
}
val Scope.application get() = (androidApplication() as MyFittApplication)
val appModule = listOf(repositoryModule, daoModule, databaseModule, viewModelModule, facadeModule)
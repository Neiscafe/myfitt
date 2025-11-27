package br.com.myfitt

import br.com.myfitt.treinos.data.repository.ExercicioRepositoryImpl
import br.com.myfitt.treinos.data.repository.ExercicioTreinoRepositoryImpl
import br.com.myfitt.treinos.data.repository.SeriesRepositoryImpl
import br.com.myfitt.treinos.data.repository.TreinoRepositoryImpl
import br.com.myfitt.treinos.domain.repository.ExercicioRepository
import br.com.myfitt.treinos.domain.repository.ExercicioTreinoRepository
import br.com.myfitt.treinos.domain.repository.SeriesRepository
import br.com.myfitt.treinos.domain.repository.TreinoRepository
import br.com.myfitt.treinos.ui.screens.exerciciosTreino.ExerciciosTreinoViewModel
import br.com.myfitt.treinos.ui.screens.listaExercicios.ListaExerciciosViewModel
import br.com.myfitt.treinos.ui.screens.menuPrincipal.MenuPrincipalViewModel
import br.com.myfitt.treinos.ui.screens.seriesExercicio.SeriesExercicioViewModel
import org.koin.core.module.dsl.viewModel
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
val viewModelModule = module {
    viewModel { ExerciciosTreinoViewModel(it[0], get()) }
    viewModel { ListaExerciciosViewModel(get()) }
    viewModel { MenuPrincipalViewModel(get()) }
    viewModel { SeriesExercicioViewModel(it[0], get()) }
}
val appModule = listOf(repositoryModule, daoModule, databaseModule, viewModelModule)
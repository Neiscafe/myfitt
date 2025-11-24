package br.com.myfitt

import br.com.myfitt.treinos.data.repository.ExercicioRepositoryImpl
import br.com.myfitt.treinos.data.repository.ExercicioTreinoRepositoryImpl
import br.com.myfitt.treinos.domain.repository.ExercicioRepository
import br.com.myfitt.treinos.domain.repository.ExercicioTreinoRepository
import br.com.myfitt.treinos.ui.screens.exerciciosTreino.ExerciciosTreinoViewModel
import br.com.myfitt.treinos.ui.screens.listaExercicios.ListaExerciciosViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {

}
val daoModule = module {

}
val repositoryModule = module {
    single<ExercicioTreinoRepository> { ExercicioTreinoRepositoryImpl() }
    single<ExercicioRepository> { ExercicioRepositoryImpl() }
}
val viewModelModule = module {
    viewModel { ExerciciosTreinoViewModel(it[0], get()) }
    viewModel { ListaExerciciosViewModel(get()) }
}
val appModule = listOf(repositoryModule, daoModule, databaseModule, viewModelModule)
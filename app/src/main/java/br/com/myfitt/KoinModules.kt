package br.com.myfitt

import br.com.myfitt.treinos.ui.screens.ExerciciosTreinoViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {

}
val daoModule = module {

}
val repositoryModule = module {

}
val viewModelModule = module {
    viewModel { ExerciciosTreinoViewModel(it[0]) }
}
val appModule = listOf(repositoryModule, daoModule, databaseModule, viewModelModule)
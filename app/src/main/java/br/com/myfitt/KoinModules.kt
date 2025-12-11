package br.com.myfitt

import br.com.myfitt.common.data.AppDatabase
import br.com.myfitt.treinos.data.dao.ExercicioDao
import br.com.myfitt.treinos.data.dao.ExercicioTreinoDao
import br.com.myfitt.treinos.data.dao.SerieExercicioDao
import br.com.myfitt.treinos.data.dao.TipoExercicioDao
import br.com.myfitt.treinos.data.dao.TreinoDao
import br.com.myfitt.treinos.data.repository.ExercicioRepositoryImpl
import br.com.myfitt.treinos.data.repository.ExercicioTreinoRepositoryImpl
import br.com.myfitt.treinos.data.repository.SeriesRepositoryImpl
import br.com.myfitt.treinos.data.repository.TipoExercicioRepositoryImpl
import br.com.myfitt.treinos.data.repository.TreinoRepositoryImpl
import br.com.myfitt.treinos.domain.facade.TreinoFacade
import br.com.myfitt.treinos.domain.repository.ExercicioRepository
import br.com.myfitt.treinos.domain.repository.ExercicioTreinoRepository
import br.com.myfitt.treinos.domain.repository.SeriesRepository
import br.com.myfitt.treinos.domain.repository.TipoExercicioRepository
import br.com.myfitt.treinos.domain.repository.TreinoRepository
import br.com.myfitt.treinos.ui.CronometroFacade
import br.com.myfitt.treinos.ui.screens.detalhesExercicio.DetalhesExercicioViewModel
import br.com.myfitt.treinos.ui.screens.exerciciosTreino.ExerciciosTreinoViewModel
import br.com.myfitt.treinos.ui.screens.listaExercicios.ListaExerciciosViewModel
import br.com.myfitt.treinos.ui.screens.listaTreinos.ListaTreinoViewModel
import br.com.myfitt.treinos.ui.screens.menuPrincipal.MenuPrincipalViewModel
import br.com.myfitt.treinos.ui.screens.seriesExercicio.SeriesExercicioViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.core.scope.Scope
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> {
        androidApplication().deleteDatabase("AppDatabase.db")
        AppDatabase.build(androidApplication()).build()
    }
}
val daoModule = module {
    single<TreinoDao> { get<AppDatabase>().treinoDao() }
    single<TipoExercicioDao> { get<AppDatabase>().tipoExercicioDao() }
    single<SerieExercicioDao> { get<AppDatabase>().serieExercicioDao() }
    single<ExercicioDao> { get<AppDatabase>().exercicioDao() }
    single<ExercicioTreinoDao> { get<AppDatabase>().exercicioTreinoDao() }
}
val repositoryModule = module {
    single<ExercicioTreinoRepository> { ExercicioTreinoRepositoryImpl(get()) }
    single<ExercicioRepository> { ExercicioRepositoryImpl(get()) }
    single<TreinoRepository> { TreinoRepositoryImpl(get()) }
    single<TipoExercicioRepository> { TipoExercicioRepositoryImpl(get()) }
    single<SeriesRepository> { SeriesRepositoryImpl(get()) }
}
val facadeModule = module {
    single { CronometroFacade(application.appScope) }
    single { TreinoFacade(get(), get(), get()) }
}
val viewModelModule = module {
    viewModel { ExerciciosTreinoViewModel(it[0], get(), get(), get()) }
    viewModel { ListaTreinoViewModel(get(), get()) }
    viewModel { DetalhesExercicioViewModel(it[0], get()) }
    viewModel { ListaExerciciosViewModel(get()) }
    viewModel { MenuPrincipalViewModel(get()) }
    viewModel { SeriesExercicioViewModel(it[0], get(), get(), get(), get()) }
}
val Scope.application get() = (androidApplication() as MyFittApplication)
val appModule = listOf(repositoryModule, daoModule, databaseModule, viewModelModule, facadeModule)
package br.com.myfitt

import org.koin.dsl.module

val databaseModule = module {

}
val daoModule = module {

}
val repositoryModule = module {

}
val viewModelModule = module {

}
val appModule = listOf(repositoryModule, daoModule, databaseModule, viewModelModule)
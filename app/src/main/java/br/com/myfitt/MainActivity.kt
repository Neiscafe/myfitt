package br.com.myfitt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import br.com.myfitt.ui.routes.cronometroTreinoRoute
import br.com.myfitt.ui.routes.listaExerciciosFichaRoute
import br.com.myfitt.ui.routes.listaExerciciosTreinoRoute
import br.com.myfitt.ui.routes.listaFichasDivisaoRoute
import br.com.myfitt.ui.routes.listaTreinosPlanilhaRoute
import br.com.myfitt.ui.theme.MyFittTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyFittTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Routes.LISTA_TREINOS_PLANILHA.name,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        cronometroTreinoRoute(navController)
                        listaTreinosPlanilhaRoute(navController)
                        listaFichasDivisaoRoute(navController)
                        listaExerciciosFichaRoute(navController)
                        listaExerciciosTreinoRoute(navController)
                    }
                }
            }
        }
    }

    companion object {
        const val PLANILHA_ID = "planilhaId"
        const val FICHA_ID = "fichaid"
        const val TREINO_ID = "treinoId"
        const val DIVISAO_ID = "divisaoId"
    }
}
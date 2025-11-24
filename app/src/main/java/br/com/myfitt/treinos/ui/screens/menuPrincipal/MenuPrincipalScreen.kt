package br.com.myfitt.treinos.ui.screens.menuPrincipal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import br.com.myfitt.treinos.ui.screens.exerciciosTreino.ExerciciosTreinoNavigation
import br.com.myfitt.treinos.ui.theme.MyFittTheme

object MenuPrincipalNavigation {
    const val route = "menuPrincipal"
    const val treinoDest = 1
    const val listaTreinoDest = 2
    fun composeNavigation(builder: NavGraphBuilder, navController: NavController) {
        builder.composable(
            route = route,
        ) {
            MenuPrincipalScreen(
                navigation = { it, id ->
                    when (it) {
                        treinoDest -> navController.navigate(ExerciciosTreinoNavigation.route + "/$id")
                    }
                })
        }
    }

}

@Composable
fun MenuPrincipalScreen(navigation: (Int, Int) -> Unit) {
//    navigation(MenuPrincipalNavigation.novoTreinoDest, 0)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp, 48.dp, 16.dp, 0.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Bem-vindo!", style = MaterialTheme.typography.headlineLarge)
        Text("O que vamos treinar hoje?", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(24.dp))
        Card2Opcoes(
            icone = {
                Icon(
                    imageVector = Icons.Default.Warning, contentDescription = "Peso de treino"
                )
            },
            titulo = "Meus treinos",
            secundario = "Hora de superar seus limites!",
            botao1 = "Histórico",
            botao2 = "Novo",
            cliqueBotao1 = {},
            cliqueBotao2 = { navigation(MenuPrincipalNavigation.treinoDest, 0) })
    }
}

@Composable
private fun Card2Opcoes(
    icone: @Composable () -> Unit,
    titulo: String,
    secundario: String,
    botao1: String,
    botao2: String,
    cliqueBotao1: () -> Unit,
    cliqueBotao2: () -> Unit,
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
                    icone()
                }
                Column {
                    Text(titulo, style = MaterialTheme.typography.titleMedium)
                    Text(
                        secundario, style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 16.dp, 0.dp, 0.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        cliqueBotao1()
                    }) { Text(botao1, color = MaterialTheme.colorScheme.primary) }
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    onClick = {
                        cliqueBotao2()
                    }) { Text(botao2) }
            }
        }
    }
}

@Preview
@Composable
private fun MenuPrincipalScreenPreview() {
    MyFittTheme {
        MenuPrincipalScreen { _, _ -> }
    }
}
package br.com.myfitt.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import br.com.myfitt.domain.models.Ficha
import br.com.myfitt.ui.components.DefaultCardList
import br.com.myfitt.ui.components.DefaultTopView
import br.com.myfitt.ui.viewModels.FichasDivisaoViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ListaFichasScreen(
    divisaoId: Int,
    navigate: (Int) -> Unit,
    viewModel: FichasDivisaoViewModel = koinViewModel(parameters = { parametersOf(divisaoId) }),
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.padding(10.dp, 30.dp, 10.dp, 0.dp)) {
        val fichas = viewModel.fichas.collectAsState(emptyList())
        val coroutineScope = rememberCoroutineScope()
        DefaultTopView("Suas fichas", hint = "Criar ficha", onComplete = {
            coroutineScope.launch { navigate(viewModel.insertFicha(nomeFicha = it)) }
        })
        DefaultCardList<Ficha>(items = fichas.value, onClick = {
            navigate(it.id)
        }, getName = { it.nome })
    }
}
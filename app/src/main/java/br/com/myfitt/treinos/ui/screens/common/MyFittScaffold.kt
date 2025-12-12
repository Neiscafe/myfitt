@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.myfitt.treinos.ui.screens.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun MyFittScaffold(
    modifier: Modifier = Modifier.padding(16.dp),
    error: String? = null,
    carregando: Boolean = false,
    limpaEventos: () -> Unit = {},
    voltar: () -> Unit = {},
    topBarTitle: @Composable () -> Unit,
    topBar: @Composable () -> Unit = {
        TopAppBar(title = topBarTitle, navigationIcon = {
            IconButton(voltar) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, "Voltar")
            }
        })
    },
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable ColumnScope.() -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    error?.let {
        scope.launch {
            snackbarHostState.showSnackbar(
                message = it
            )
            limpaEventos()
        }
    }
    if (carregando) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }, topBar = topBar) {
        Column(
            modifier
                .padding(it)
                .fillMaxSize(),
            verticalArrangement = verticalArrangement
        ) {
            content(this)
        }
    }
}
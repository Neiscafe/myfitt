package br.com.myfitt.ui.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun ViewModel.io(action: suspend ()->Unit){
    viewModelScope.launch(Dispatchers.IO){
        action()
    }
}
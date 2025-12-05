package br.com.myfitt.common.utils

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

data class MutexWrapper<T : Any?>(val data: T) {
    suspend inline fun runLocking(block: (T) -> Unit) {
        mutex.withLock {
            block(data)
        }
    }
    val mutex = Mutex()
}
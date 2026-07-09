package xyz.fsg123.loveon.core

sealed interface UiState<out T> {
    object Loading : UiState<Nothing>
    data class Success<out T>(val data: T) : UiState<T>
    data class Error(val throwable: Throwable) : UiState<Nothing>
}
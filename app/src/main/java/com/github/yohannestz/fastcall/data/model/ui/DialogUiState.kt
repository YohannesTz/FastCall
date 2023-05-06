package com.github.yohannestz.fastcall.data.model.ui

sealed interface DialogUiState {

    object Loading: DialogUiState

    data class AddState(
        val isSuccessful: Boolean
    ) : DialogUiState

    data class Error(
        val throwable: String? = null
    ) : DialogUiState
}

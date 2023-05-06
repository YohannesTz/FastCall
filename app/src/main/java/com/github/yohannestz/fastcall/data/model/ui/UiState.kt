package com.github.yohannestz.fastcall.data.model.ui

import com.github.yohannestz.fastcall.data.model.db.Contact

sealed interface UiState {
    object Loading : UiState

    data class Success(
        val data: List<Contact>
    ) : UiState

    data class Error(
        val throwable: String? = null
    ) : UiState
}

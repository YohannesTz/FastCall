package com.github.yohannestz.fastcall.ui

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.yohannestz.fastcall.data.model.db.Contact
import com.github.yohannestz.fastcall.data.model.generic.Result
import com.github.yohannestz.fastcall.data.model.ui.DialogUiState
import com.github.yohannestz.fastcall.data.model.ui.UiState
import com.github.yohannestz.fastcall.data.repo.ContactsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val contactsRepository: ContactsRepository,
) : ViewModel() {

    private val _contactsFlow = MutableStateFlow<UiState>(UiState.Loading)
    val contactsFlow: StateFlow<UiState> = _contactsFlow.asStateFlow()

    private val _dialogFlow = MutableStateFlow<DialogUiState>(DialogUiState.Loading)
    val dialogFlow: StateFlow<DialogUiState> = _dialogFlow.asStateFlow()

    init {
        getAllContacts()
    }

    fun getAllContacts() {
        viewModelScope.launch {
            contactsRepository.getAllContacts()
                .collect { result ->
                    _contactsFlow.update {
                        when (result) {
                            is Result.Loading -> UiState.Loading
                            is Result.Success -> UiState.Success(result.data ?: emptyList())
                            is Result.Error -> UiState.Error(result.message ?: "Unknown Error")
                        }
                    }
                }
        }
    }

    fun addContacts(contactName: String, phoneNumber: String) {
        viewModelScope.launch {
            contactsRepository.insertContact(
                Contact(
                    phoneNumber = phoneNumber,
                    name = contactName,
                    photoUrl = "",
                    isFav = false
                )
            ).collect { result ->
                _dialogFlow.update {
                    when (result) {
                        is Result.Loading -> DialogUiState.Loading
                        is Result.Success -> DialogUiState.AddState(true)
                        is Result.Error -> DialogUiState.Error(result.message ?: "Unknown Error")
                    }
                }
            }
        }
    }
}
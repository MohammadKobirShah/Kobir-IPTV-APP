package com.kobir.iptv.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kobir.iptv.domain.usecase.LoadPlaylistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SetupUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val loadPlaylistUseCase: LoadPlaylistUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SetupUiState())
    val uiState: StateFlow<SetupUiState> = _uiState.asStateFlow()

    fun loadPlaylist(url: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = SetupUiState(isLoading = true)
            val result = loadPlaylistUseCase(url)
            result.fold(
                onSuccess = { onSuccess() },
                onFailure = { _uiState.value = SetupUiState(error = it.message) }
            )
        }
    }
}

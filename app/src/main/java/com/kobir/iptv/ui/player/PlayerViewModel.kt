package com.kobir.iptv.ui.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kobir.iptv.data.model.Channel
import com.kobir.iptv.data.repository.ChannelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlayerUiState(
    val channel: Channel? = null,
    val allChannels: List<Channel> = emptyList(),
    val currentIndex: Int = 0
)

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val repository: ChannelRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    fun loadChannel(channelId: Long) {
        viewModelScope.launch {
            val channels = repository.allChannels.first()
            val index = channels.indexOfFirst { it.id == channelId }
            if (index >= 0) {
                _uiState.value = PlayerUiState(
                    channel = channels[index],
                    allChannels = channels,
                    currentIndex = index
                )
            }
        }
    }

    fun playNext() {
        val state = _uiState.value
        val channels = state.allChannels
        if (channels.isEmpty()) return
        val nextIndex = (state.currentIndex + 1) % channels.size
        _uiState.value = state.copy(
            channel = channels[nextIndex],
            currentIndex = nextIndex
        )
    }

    fun playPrevious() {
        val state = _uiState.value
        val channels = state.allChannels
        if (channels.isEmpty()) return
        val prevIndex = if (state.currentIndex - 1 < 0) channels.size - 1 else state.currentIndex - 1
        _uiState.value = state.copy(
            channel = channels[prevIndex],
            currentIndex = prevIndex
        )
    }

    fun toggleFavorite() {
        val channel = _uiState.value.channel ?: return
        viewModelScope.launch {
            repository.toggleFavorite(channel.id, !channel.isFavorite)
            _uiState.value = _uiState.value.copy(
                channel = channel.copy(isFavorite = !channel.isFavorite)
            )
        }
    }
}

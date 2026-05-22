package com.kobir.iptv.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kobir.iptv.data.repository.ChannelRepository
import kotlinx.coroutines.flow.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    channelRepository: ChannelRepository
) : ViewModel() {

    val hasChannels: StateFlow<Boolean> = channelRepository.allChannels.map { it.isNotEmpty() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)
}

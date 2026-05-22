package com.kobir.iptv.ui.epg

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kobir.iptv.data.model.Channel
import com.kobir.iptv.data.repository.ChannelRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class EPGViewModel @Inject constructor(
    repository: ChannelRepository
) : ViewModel() {

    val channels: StateFlow<List<Channel>> = repository.allChannels
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val categories: StateFlow<List<String>> = repository.categories
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}

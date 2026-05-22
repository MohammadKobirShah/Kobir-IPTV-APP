package com.kobir.iptv.domain.usecase

import com.kobir.iptv.data.repository.ChannelRepository
import javax.inject.Inject

class LoadPlaylistUseCase @Inject constructor(
    private val repository: ChannelRepository
) {
    suspend operator fun invoke(url: String) = repository.loadPlaylist(url)
}

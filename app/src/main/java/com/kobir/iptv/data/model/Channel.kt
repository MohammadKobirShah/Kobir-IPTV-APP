package com.kobir.iptv.data.model

data class Channel(
    val id: Long = 0,
    val name: String = "",
    val url: String = "",
    val logoUrl: String? = null,
    val category: String? = null,
    val group: String? = null,
    val epgChannelId: String? = null,
    val isFavorite: Boolean = false
)

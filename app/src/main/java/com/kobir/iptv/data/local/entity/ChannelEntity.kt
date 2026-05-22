package com.kobir.iptv.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "channels")
data class ChannelEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String = "",
    val url: String = "",
    @ColumnInfo(name = "logo_url")
    val logoUrl: String? = null,
    val category: String? = null,
    @ColumnInfo(name = "\"group\"")
    val group: String? = null,
    @ColumnInfo(name = "epg_channel_id")
    val epgChannelId: String? = null,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false
)

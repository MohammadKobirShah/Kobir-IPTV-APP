package com.kobir.iptv.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kobir.iptv.data.local.dao.CategoryDao
import com.kobir.iptv.data.local.dao.ChannelDao
import com.kobir.iptv.data.local.entity.CategoryEntity
import com.kobir.iptv.data.local.entity.ChannelEntity

@Database(
    entities = [ChannelEntity::class, CategoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun channelDao(): ChannelDao
    abstract fun categoryDao(): CategoryDao
}

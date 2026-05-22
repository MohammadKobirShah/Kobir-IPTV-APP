package com.kobir.iptv.data.repository

import com.kobir.iptv.data.local.dao.CategoryDao
import com.kobir.iptv.data.local.dao.ChannelDao
import com.kobir.iptv.data.local.entity.CategoryEntity
import com.kobir.iptv.data.local.entity.ChannelEntity
import com.kobir.iptv.data.model.Channel
import com.kobir.iptv.data.parser.M3UParser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChannelRepository @Inject constructor(
    private val channelDao: ChannelDao,
    private val categoryDao: CategoryDao,
    private val parser: M3UParser
) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .followRedirects(true)
        .build()

    val allChannels: Flow<List<Channel>> = channelDao.getAllChannels().map { entities ->
        entities.map { it.toModel() }
    }

    val categories: Flow<List<String>> = categoryDao.getAllCategories().map { entities ->
        entities.map { it.name }
    }

    fun getChannelsByCategory(category: String): Flow<List<Channel>> {
        return channelDao.getChannelsByCategory(category).map { entities ->
            entities.map { it.toModel() }
        }
    }

    fun getFavoriteChannels(): Flow<List<Channel>> {
        return channelDao.getFavoriteChannels().map { entities ->
            entities.map { it.toModel() }
        }
    }

    fun searchChannels(query: String): Flow<List<Channel>> {
        return channelDao.searchChannels(query).map { entities ->
            entities.map { it.toModel() }
        }
    }

    suspend fun loadPlaylist(url: String): Result<Int> {
        return try {
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val body = response.body?.string() ?: return Result.failure(Exception("Empty response"))

            val channels = parser.parse(body)
            channelDao.deleteAll()
            categoryDao.deleteAll()

            val entities = channels.map { it.toEntity() }
            channelDao.insertAll(entities)

            val categories = channels
                .mapNotNull { it.category }
                .distinct()
                .sorted()
                .map { CategoryEntity(name = it, channelCount = channels.count { c -> c.category == it }) }
            categoryDao.insertAll(categories)

            Result.success(channels.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleFavorite(channelId: Long, isFavorite: Boolean) {
        channelDao.toggleFavorite(channelId, isFavorite)
    }

    private fun Channel.toEntity() = ChannelEntity(
        id = id,
        name = name,
        url = url,
        logoUrl = logoUrl,
        category = category,
        group = group,
        epgChannelId = epgChannelId,
        isFavorite = isFavorite
    )

    private fun ChannelEntity.toModel() = Channel(
        id = id,
        name = name,
        url = url,
        logoUrl = logoUrl,
        category = category,
        group = group,
        epgChannelId = epgChannelId,
        isFavorite = isFavorite
    )
}

package com.kobir.iptv.data.parser

import com.kobir.iptv.data.model.Channel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class M3UParser @Inject constructor() {

    fun parse(m3uContent: String): List<Channel> {
        val channels = mutableListOf<Channel>()
        val lines = m3uContent.lines()
        var index = 0L
        var i = 0

        while (i < lines.size) {
            val line = lines[i].trim()
            if (line.startsWith("#EXTINF:")) {
                val urlLine = lines.getOrNull(i + 1)?.trim() ?: ""
                if (urlLine.isNotEmpty() && !urlLine.startsWith("#")) {
                    val channel = parseExtInf(line, urlLine, index)
                    channels.add(channel)
                    index++
                    i += 2
                    continue
                }
            }
            i++
        }
        return channels
    }

    private fun parseExtInf(extInfLine: String, url: String, id: Long): Channel {
        val name = extractName(extInfLine)
        val logoUrl = extractAttribute(extInfLine, "tvg-logo")
        val category = extractAttribute(extInfLine, "group-title")
        val epgChannelId = extractAttribute(extInfLine, "tvg-id")

        return Channel(
            id = id,
            name = name,
            url = url,
            logoUrl = logoUrl.ifEmpty { null },
            category = category.ifEmpty { null },
            epgChannelId = epgChannelId.ifEmpty { null },
            group = category.ifEmpty { null }
        )
    }

    private fun extractName(extInfLine: String): String {
        val commaIndex = extInfLine.lastIndexOf(',')
        return if (commaIndex >= 0) {
            extInfLine.substring(commaIndex + 1).trim()
        } else {
            "Unknown"
        }
    }

    private fun extractAttribute(extInfLine: String, attribute: String): String {
        val regex = """$attribute="([^"]*)"""".toRegex()
        return regex.find(extInfLine)?.groupValues?.getOrElse(1) { "" } ?: ""
    }
}

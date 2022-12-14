package me.sknz.ousubot.domain.dto

import net.dv8tion.jda.api.interactions.DiscordLocale
import java.io.Serializable
import java.util.*

data class BeatmapSearchRequest(
    var query: String,
    val locale: Locale,
    var beatmapSet: Int? = null
) : Serializable {

    constructor(query: String, locale: DiscordLocale, beatmapSet: Int? = null): this(query, Locale.forLanguageTag(locale.locale), beatmapSet)

    init {
        this.query = Base64.getEncoder().encodeToString(this.query.toByteArray())
    }

    fun withBeatmapSetId(beatmapSet: Int): BeatmapSearchRequest {
        this.beatmapSet = beatmapSet
        return this
    }

    fun decode(): String {
        return String(Base64.getDecoder().decode(query))
    }
}
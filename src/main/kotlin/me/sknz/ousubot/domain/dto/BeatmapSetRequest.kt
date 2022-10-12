package me.sknz.ousubot.domain.dto

import net.dv8tion.jda.api.interactions.DiscordLocale
import java.io.Serializable
import java.util.Locale

data class BeatmapSetRequest(
    var id: Int?,
    var beatmap: Int?,
    val locale: Locale
): Serializable {
    constructor(id: Int?, beatmap: Int?, locale: DiscordLocale) : this(id, beatmap, Locale.forLanguageTag(locale.locale))
}
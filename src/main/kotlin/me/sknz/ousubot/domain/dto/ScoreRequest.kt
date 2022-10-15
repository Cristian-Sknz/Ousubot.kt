package me.sknz.ousubot.domain.dto

import net.dv8tion.jda.api.interactions.DiscordLocale
import java.io.Serializable
import java.util.Locale

data class ScoreRequest(
    val beatmap: Int,
    val score: Long?,
    val locale: Locale
) : Serializable {
    constructor(beatmap: Int, score: Long?, locale: DiscordLocale) : this(beatmap, score, Locale.forLanguageTag(locale.locale))
}
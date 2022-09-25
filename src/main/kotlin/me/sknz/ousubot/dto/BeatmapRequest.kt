package me.sknz.ousubot.dto

import net.dv8tion.jda.api.interactions.DiscordLocale
import java.io.Serializable
import java.util.*

data class BeatmapRequest(
    val id: Int,
    val locale: Locale
): Serializable {
    constructor(id: Int, user: DiscordLocale): this(id, Locale.forLanguageTag(user.locale))
}

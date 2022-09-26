package me.sknz.ousubot.dto

import me.sknz.ousubot.api.models.beatmaps.Beatmap
import net.dv8tion.jda.api.entities.MessageEmbed

/**
 * ## BeatmapSetDiscordEmbed
 *
 * Classe utilizada para armazenar dados de um embed
 * que contém um [me.sknz.ousubot.api.models.beatmaps.BeatmapSet].
 */
data class BeatmapSetDiscordEmbed(
    val embed: BeatmapDiscordEmbed,
    val next: Int?,
    val back: Int?,
    val beatmap: Beatmap
) {
    fun hasNext() = next != null
    fun hasBack() = back != null

    fun toMessageEmbed(): MessageEmbed {
        return embed.toMessageEmbed()
    }
}
package me.sknz.ousubot.dto

import me.sknz.ousubot.api.models.beatmaps.Beatmap
import me.sknz.ousubot.core.xml.DiscordEmbed
import java.io.Serializable

/**
 * ## DiscordBeatmapEmbed
 *
 * Classe utilizada para armazenar um embed que contém um
 * [me.sknz.ousubot.api.models.beatmaps.BeatmapSet]
 * e a ordem em que está numa lista.
 *
 * @see AbstractDiscordEmbed
 */
class DiscordBeatmapEmbed(
    embed: DiscordEmbed,
    next: Int?,
    back: Int?,
    beatmap: Beatmap
): AbstractDiscordEmbed<Beatmap>(embed, next, back, beatmap), Serializable
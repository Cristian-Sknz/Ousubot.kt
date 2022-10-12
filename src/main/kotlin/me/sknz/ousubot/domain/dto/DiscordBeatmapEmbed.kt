package me.sknz.ousubot.domain.dto

import me.sknz.ousubot.app.api.models.beatmaps.Beatmap
import me.sknz.ousubot.app.api.models.beatmaps.BeatmapSet
import me.sknz.ousubot.infrastructure.xml.DiscordEmbed
import java.io.Serializable

/**
 * ## DiscordBeatmapEmbed
 *
 * Classe utilizada para armazenar um embed que contém um
 * [BeatmapSet]
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
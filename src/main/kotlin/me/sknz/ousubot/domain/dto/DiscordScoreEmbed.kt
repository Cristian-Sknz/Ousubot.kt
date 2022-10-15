package me.sknz.ousubot.domain.dto

import me.sknz.ousubot.app.api.models.scores.BeatmapScore
import me.sknz.ousubot.infrastructure.xml.DiscordEmbed
import java.io.Serializable

/**
 * ## DiscordScoreEmbed
 *
 * Classe utilizada para armazenar um embed que contém um
 * [BeatmapScore] e um Id de Beatmap
 *
 * @see AbstractDiscordEmbed
 */
class DiscordScoreEmbed(
    embed: DiscordEmbed,
    next: Long?,
    back: Long?,
    beatmapId: Int
): AbstractDiscordEmbed<Int>(embed, next, back, beatmapId), Serializable
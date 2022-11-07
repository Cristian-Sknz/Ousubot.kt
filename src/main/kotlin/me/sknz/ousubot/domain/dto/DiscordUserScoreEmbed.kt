package me.sknz.ousubot.domain.dto

import me.sknz.ousubot.app.api.models.scores.BeatmapScore
import me.sknz.ousubot.app.api.params.UserScoreParameter
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
class DiscordUserScoreEmbed(
    embed: DiscordEmbed,
    next: Long?,
    back: Long?,
    parameters: Pair<Int, UserScoreParameter>
): AbstractDiscordEmbed<Pair<Int, UserScoreParameter>>(embed, next, back, parameters), Serializable
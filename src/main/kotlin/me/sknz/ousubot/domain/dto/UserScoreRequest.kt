package me.sknz.ousubot.domain.dto

import me.sknz.ousubot.app.api.params.UserScoreParameter
import net.dv8tion.jda.api.interactions.DiscordLocale
import java.io.Serializable
import java.util.Locale

data class UserScoreRequest(
    val userId: Int,
    val parameters: UserScoreParameter,
    var score: Long?,
    val locale: Locale
) : Serializable {
    constructor(
        userId: Int,
        parameters: UserScoreParameter,
        score: Long?,
        locale: DiscordLocale
    ): this(userId, parameters, score, Locale.forLanguageTag(locale.locale))
}
package me.sknz.ousubot.dto

import net.dv8tion.jda.api.interactions.DiscordLocale
import java.io.Serializable
import java.util.*

class UserRequest(
    var username: String,
    val locale: Locale
): Serializable {
    constructor(username: String, locale: DiscordLocale) : this(username, Locale.forLanguageTag(locale.locale))
}
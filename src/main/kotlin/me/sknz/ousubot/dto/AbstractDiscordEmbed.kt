package me.sknz.ousubot.dto

import me.sknz.ousubot.core.xml.DiscordEmbed
import net.dv8tion.jda.api.entities.MessageEmbed
import java.io.Serializable

/**
 * ## AbstractDiscordEmbed
 *
 * Classe abstrata para armazenar um [DiscordEmbed],
 * um payload e a ordem em que ele está em uma lista.
 *
 * @see DiscordEmbed
 */
abstract class AbstractDiscordEmbed<T>(
    val embed: DiscordEmbed,
    val next: Int?,
    val back: Int?,
    val payload: T
): Serializable {

    constructor(embed: DiscordEmbed, payload: T): this(embed, null, null, payload)
    fun hasNext(): Boolean = next != null
    fun hasBack(): Boolean = back != null
    fun toMessageEmbed(): MessageEmbed = embed.toMessageEmbed()
}
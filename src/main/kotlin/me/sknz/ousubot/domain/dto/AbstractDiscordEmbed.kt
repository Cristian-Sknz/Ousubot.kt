package me.sknz.ousubot.domain.dto

import me.sknz.ousubot.infrastructure.xml.DiscordEmbed
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
    val next: Long?,
    val back: Long?,
    val payload: T
): Serializable {

    constructor(embed: DiscordEmbed, payload: T): this(embed, null as  Long?, null as Long?, payload)
    constructor(embed: DiscordEmbed, next: Int?, back: Int?, payload: T): this(embed, next?.toLong(), back?.toLong(), payload)

    fun hasNext(): Boolean = next != null
    fun hasBack(): Boolean = back != null
    fun toMessageEmbed(): MessageEmbed = embed.toMessageEmbed()
}
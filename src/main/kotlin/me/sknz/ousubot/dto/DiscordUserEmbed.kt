package me.sknz.ousubot.dto

import me.sknz.ousubot.api.models.users.User
import me.sknz.ousubot.core.xml.DiscordEmbed
import java.awt.image.BufferedImage
import java.io.Serializable

/**
 * ## DiscordUserEmbed
 *
 * Classe utilizada para armazenar um embed que contém um
 * [me.sknz.ousubot.api.models.users.User]
 * e a imagem gerada pelo bot.
 *
 * @see AbstractDiscordEmbed
 */
class DiscordUserEmbed(
    embed: DiscordEmbed,
    payload: DiscordUserPayload
): AbstractDiscordEmbed<DiscordUserEmbed.DiscordUserPayload>(embed, payload), Serializable {

    class DiscordUserPayload(
        val user: User,
        val image: BufferedImage,
    ): Serializable
}
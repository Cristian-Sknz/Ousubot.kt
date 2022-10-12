package me.sknz.ousubot.domain.dto

import me.sknz.ousubot.app.api.models.users.User
import me.sknz.ousubot.infrastructure.xml.DiscordEmbed
import java.awt.image.BufferedImage
import java.io.Serializable

/**
 * ## DiscordUserEmbed
 *
 * Classe utilizada para armazenar um embed que contém um
 * [User]
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
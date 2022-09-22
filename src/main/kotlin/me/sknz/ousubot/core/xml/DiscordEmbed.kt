package me.sknz.ousubot.core.xml

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import java.awt.Color

/**
 * ## DiscordEmbed
 *
 * Classe utilizada para serialização de XML que posteriormente
 * serão transformados em [net.dv8tion.jda.api.entities.MessageEmbed]
 *
 * @see net.dv8tion.jda.api.entities.MessageEmbed
 * @see com.fasterxml.jackson.dataformat.xml.XmlMapper
 */
data class DiscordEmbed(
    var title: EmbedValue? = null,
    var description: String? = null,
    var thumbnail: String? = null,
    var image: String? = null,
    var color: Int? = null,
    var fields: List<EmbedField> = emptyList(),
    var author: EmbedValue? = null,
    var footer: EmbedValue? = null,
) {

    data class EmbedField(
        var label: String = "",
        var value: String = "",
        var inline: Boolean = false
    )

    data class EmbedValue(
        var name: String? = null,
        var url: String? = null,
        var icon: String? = null,
    )

    companion object {
        fun MessageEmbed.toDiscordEmbed(): DiscordEmbed {
            return DiscordEmbed(
                EmbedValue(this.title, this.url),
                this.description,
                this.thumbnail?.url,
                this.image?.url,
                this.color?.rgb,
                this.fields.map { EmbedField(it.name!!, it.value!!, it.isInline) },
                this.author?.let { EmbedValue(this.author!!.name, this.author!!.url, this.author!!.iconUrl) },
                this.footer?.let { EmbedValue(this.footer!!.text, null, this.footer!!.iconUrl) }
            )
        }
    }

    fun toMessageEmbed(): MessageEmbed {
        return EmbedBuilder()
            .setTitle(title?.name, title?.url)
            .setDescription(description)
            .setImage(image)
            .setThumbnail(thumbnail)
            .setColor(color?.let { Color(it) })
            .setAuthor(author?.name, author?.url, author?.icon)
            .setFooter(footer?.name, footer?.url)
            .let {
                fields.forEach { field ->
                    it.addField(field.label, field.value, field.inline)
                }
                it
            }
            .build()
    }
}
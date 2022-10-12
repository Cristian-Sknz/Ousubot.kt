package me.sknz.ousubot.infrastructure.xml

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import java.awt.Color
import java.io.Serializable
import java.time.OffsetDateTime

/**
 * ## DiscordEmbed
 *
 * Classe utilizada para serialização de XML que posteriormente
 * serão transformados em [net.dv8tion.jda.api.entities.MessageEmbed]
 *
 * @see net.dv8tion.jda.api.entities.MessageEmbed
 * @see com.fasterxml.jackson.dataformat.xml.XmlMapper
 */
open class DiscordEmbed(
    var title: EmbedValue? = null,
    var description: String? = null,
    var thumbnail: String? = null,
    var image: String? = null,
    var color: Int? = null,
    var fields: List<EmbedField> = emptyList(),
    var author: EmbedValue? = null,
    var footer: EmbedValue? = null,
    var timestamp: OffsetDateTime? = null
): Serializable {

    data class EmbedField(
        var label: String = "",
        var value: String = "",
        var inline: Boolean = false
    ): Serializable

    data class EmbedValue(
        var name: String? = null,
        var url: String? = null,
        var icon: String? = null,
    ): Serializable

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
                this.footer?.let { EmbedValue(this.footer!!.text, null, this.footer!!.iconUrl) },
                this.timestamp
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
            .setFooter(footer?.name, footer?.icon)
            .let {
                fields.forEach { field ->
                    it.addField(field.label, field.value, field.inline)
                }
                if (timestamp != null) {
                    it.setTimestamp(timestamp)
                }
                it
            }
            .build()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DiscordEmbed

        if (title != other.title) return false
        if (description != other.description) return false
        if (thumbnail != other.thumbnail) return false
        if (image != other.image) return false
        if (color != other.color) return false
        if (fields != other.fields) return false
        if (author != other.author) return false
        if (footer != other.footer) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title?.hashCode() ?: 0
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (thumbnail?.hashCode() ?: 0)
        result = 31 * result + (image?.hashCode() ?: 0)
        result = 31 * result + (color ?: 0)
        result = 31 * result + fields.hashCode()
        result = 31 * result + (author?.hashCode() ?: 0)
        result = 31 * result + (footer?.hashCode() ?: 0)
        return result
    }
}
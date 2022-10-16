package me.sknz.ousubot.infrastructure.tools

import com.fasterxml.jackson.databind.ObjectMapper
import me.sknz.ousubot.infrastructure.annotations.WorkInProgress
import net.dv8tion.jda.api.entities.emoji.CustomEmoji
import net.dv8tion.jda.api.entities.emoji.Emoji
import org.springframework.core.io.ClassPathResource

/**
 * ## CustomEmojis
 *
 * Objeto que contém todos os [CustomEmoji]s em `classpath:assets/emoji.json`
 * que poderão ficar disponíveis para o ser utilizados
 *
 * @see CustomEmoji
 */
@WorkInProgress
object CustomEmojis {

    private val emojis: List<CustomEmoji> = getAvailableEmojis()

    operator fun get(name: String?): CustomEmoji? {
        return name?.let { value -> emojis.find { it.name.equals(value, true) } }
    }

    fun CustomEmoji.xml(): String {
        return "&lt;:${this.name}:${this.id}&gt;"
    }

    private fun getAvailableEmojis(): List<CustomEmoji> {
        val path = ClassPathResource("assets/emoji.json", this.javaClass.classLoader)
        if (!path.exists()) {
            return emptyList()
        }
        val json = ObjectMapper().readTree(path.inputStream)
        return json.map { Emoji.fromCustom(it.get("name").asText(), it.get("id").asLong(), false) }
    }

    override fun toString(): String {
        return emojis.toString()
    }
}
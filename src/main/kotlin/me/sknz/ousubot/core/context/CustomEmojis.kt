package me.sknz.ousubot.core.context

import com.fasterxml.jackson.databind.ObjectMapper
import me.sknz.ousubot.api.annotations.WorkInProgress
import net.dv8tion.jda.api.entities.emoji.CustomEmoji
import net.dv8tion.jda.api.entities.emoji.Emoji
import org.springframework.core.io.ClassPathResource

@WorkInProgress
class CustomEmojis {

    private val emoji: ArrayList<CustomEmoji> = arrayListOf()

    init {
        val path = ClassPathResource("assets/emoji.json", this.javaClass.classLoader)
        if (path.exists()) {
            val json = ObjectMapper().readTree(path.inputStream)
            for (node in json) {
                emoji.add(Emoji.fromCustom(node.get("name").asText(), node.get("id").asLong(), false))
            }
        }
    }

    fun getEmoji(name: String): CustomEmoji? {
        return emoji.find { it.name.equals(name, true) }
    }

    fun getXMLEmoji(name: String): String? {
        return getEmoji(name)?.let {
            "&lt;:${it.name}:${it.id}&gt;"
        }
    }
}
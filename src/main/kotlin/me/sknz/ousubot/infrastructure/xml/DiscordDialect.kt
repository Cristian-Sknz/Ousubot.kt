package me.sknz.ousubot.infrastructure.xml

import me.sknz.ousubot.infrastructure.tools.CustomEmojis
import me.sknz.ousubot.infrastructure.xml.processor.EmojiProcessor
import org.thymeleaf.dialect.AbstractProcessorDialect
import org.thymeleaf.processor.IProcessor

/**
 * ## DiscordDialect
 *
 * Dialeto customizado para processar alguns dos elementos
 * de um [net.dv8tion.jda.api.entities.MessageEmbed] como Emotes
 */
class DiscordDialect(
    private val emojis: CustomEmojis
) : AbstractProcessorDialect("Discord Dialect", "discord", 1000) {

    override fun getProcessors(dialectPrefix: String): Set<IProcessor> {
        val processors: MutableSet<IProcessor> = HashSet()
        processors.add(EmojiProcessor(dialectPrefix, emojis))
        return processors
    }
}
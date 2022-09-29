package me.sknz.ousubot.commands

import me.sknz.ousubot.core.annotations.ExceptionHandler
import me.sknz.ousubot.core.exceptions.AbstractExceptionHandler
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import java.util.*

@Component
class CommandExceptionHandler(
    val source: MessageSource
) : AbstractExceptionHandler() {

    @ExceptionHandler(NotImplementedError::class)
    fun handleNotImplemented(interaction: SlashCommandInteractionEvent) {
        interaction.send(source.getMessage("exceptions.notimplemented", null,
            Locale.forLanguageTag(interaction.userLocale.locale)))
    }

    private fun SlashCommandInteractionEvent.send(message: String) {
        try {
            this.reply(message).setEphemeral(true).queue()
        } catch (_: Exception) { interaction.hook.sendMessage(message).queue() }
    }

}
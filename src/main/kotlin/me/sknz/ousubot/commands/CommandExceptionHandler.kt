package me.sknz.ousubot.commands

import feign.FeignException.InternalServerError
import feign.FeignException.NotFound
import feign.FeignException.ServiceUnavailable
import feign.FeignException.Unauthorized
import me.sknz.ousubot.core.annotations.ExceptionHandler
import me.sknz.ousubot.core.exceptions.AbstractExceptionHandler
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import org.springframework.context.NoSuchMessageException
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.stereotype.Component
import java.util.*

/**
 * ## CommandExceptionHandler
 *
 * Classe responsável para lidar com exceções envolvendo comandos.
 * Ao ocorrer uma exceção/erro o usuário será respondido com está classe.
 */
@Component
class CommandExceptionHandler(
    val source: ReloadableResourceBundleMessageSource
) : AbstractExceptionHandler() {

    @ExceptionHandler(NotImplementedError::class)
    fun handleNotImplemented(interaction: SlashCommandInteractionEvent, exception: Throwable) {
        interaction.send(source.getMessage("exceptions.notimplemented", null,
            Locale.forLanguageTag(interaction.userLocale.locale)))
    }

    @ExceptionHandler(NotFound::class)
    fun handleNotFound(interaction: SlashCommandInteractionEvent, exception: NotFound) {
        try {
            val command = interaction.commandPath.replace("/", ".")
            interaction.send(source.getMessage("osu.exceptions.notfound.$command", null,
                Locale.forLanguageTag(interaction.userLocale.locale)))
        } catch (e: NoSuchMessageException) {
            interaction.send(source.getMessage("osu.exceptions.notfound", null,
                Locale.forLanguageTag(interaction.userLocale.locale)))
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    @ExceptionHandler(Unauthorized::class)
    fun handleUnauthorized(interaction: SlashCommandInteractionEvent) {
        interaction.send(source.getMessage("osu.exceptions.unauthorized", null,
            Locale.forLanguageTag(interaction.userLocale.locale)))
    }

    @ExceptionHandler(ServiceUnavailable::class)
    fun handleServiceUnavailable(interaction: SlashCommandInteractionEvent) {
        interaction.send(source.getMessage("osu.exceptions.internal.server", null,
            Locale.forLanguageTag(interaction.userLocale.locale)))
    }

    @ExceptionHandler(InternalServerError::class)
    fun handleInternalServerError(interaction: SlashCommandInteractionEvent) {
        interaction.send(source.getMessage("osu.exceptions.internal.server", null,
            Locale.forLanguageTag(interaction.userLocale.locale)))
    }

    private fun SlashCommandInteractionEvent.send(message: String) {
        try {
            this.reply(message).setEphemeral(true).queue(null) {
                if (it is IllegalStateException) interaction.hook.sendMessage(message).queue()
            }
        } catch (_: Exception) { interaction.hook.sendMessage(message).queue() }
    }
}
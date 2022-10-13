package me.sknz.ousubot.app.commands

import feign.FeignException.*
import me.sknz.ousubot.infrastructure.annotations.ExceptionHandler
import me.sknz.ousubot.infrastructure.exceptions.AbstractExceptionHandler
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.interactions.commands.CommandInteraction
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
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
    val source: ReloadableResourceBundleMessageSource,
) : AbstractExceptionHandler() {

    val logger: Logger = LogManager.getLogger(this::class.java)

    override fun onException(event: GenericEvent?, throwable: Throwable) {
        logger.error("Ocorreu um erro ao tentar executar o comando (" +
                "${(event as CommandInteraction).commandPath})", throwable)
        super.onException(event, throwable)
    }

    @ExceptionHandler(NotImplementedError::class)
    fun handleNotImplemented(interaction: CommandInteraction, exception: Throwable) {
        interaction.send(source.getMessage("exceptions.notimplemented", null,
            Locale.forLanguageTag(interaction.userLocale.locale)))
    }

    @ExceptionHandler(NotFound::class)
    fun handleNotFound(interaction: CommandInteraction, exception: NotFound) {
        try {
            val command = interaction.commandPath
                .replace("/", ".")
                .replace(" ", "_")
                .lowercase()

            val code = "osu.exceptions.notfound.$command"
            val message = source.getMessage(code, null,
                Locale.forLanguageTag(interaction.userLocale.locale))

            if (message.equals(code, true)){
                interaction.send(source.getMessage("osu.exceptions.notfound", null,
                    Locale.forLanguageTag(interaction.userLocale.locale)))
                return
            }

            interaction.send(message)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    @ExceptionHandler(Unauthorized::class)
    fun handleUnauthorized(interaction: CommandInteraction) {
        interaction.send(source.getMessage("osu.exceptions.unauthorized", null,
            Locale.forLanguageTag(interaction.userLocale.locale)))
    }

    @ExceptionHandler(ServiceUnavailable::class)
    fun handleServiceUnavailable(interaction: CommandInteraction) {
        interaction.send(source.getMessage("osu.exceptions.internal.server", null,
            Locale.forLanguageTag(interaction.userLocale.locale)))
    }

    @ExceptionHandler(InternalServerError::class)
    fun handleInternalServerError(interaction: CommandInteraction) {
        interaction.send(source.getMessage("osu.exceptions.internal.server", null,
            Locale.forLanguageTag(interaction.userLocale.locale)))
    }

    private fun CommandInteraction.send(message: String) {
        try {
            this.reply(message).setEphemeral(true).queue(null) {
                if (it is IllegalStateException) this.hook.sendMessage(message).queue()
            }
        } catch (_: Exception) { this.hook.sendMessage(message).queue() }
    }
}
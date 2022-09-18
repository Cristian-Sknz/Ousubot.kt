package me.sknz.ousubot.core.annotations

import org.springframework.stereotype.Component
import java.lang.annotation.Inherited

/**
 * Anotação para declarar uma classe como um SlashCommandController
 * Ao declarar uma classe como SlashCommandController, ela começa a se comportar
 * como uma rota para executar comandos do bot.
 *
 * Se a propriedade [SlashCommandController.name] não for vazia, está classe irá se comportar como um
 * comando com subcomandos.
 * ```
 * @SlashCommandController(name="hello")
 * class MyAwesomeCommand {
 *
 *      @SlashCommand(name="world", description="Dê um alô para o mundo!")
 *      fun subcommand() {}
 * }
 * ```
 *
 *
 * @see SlashCommand
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Component
@Inherited
annotation class SlashCommandController(
    /**
     * Ao preencher esta propriedade a classe que for instanciada com
     * está anotação irá se comportar como um comando com subcomandos.
     */
    val name: String = ""
)
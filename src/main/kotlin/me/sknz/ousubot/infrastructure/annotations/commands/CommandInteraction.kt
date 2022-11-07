package me.sknz.ousubot.infrastructure.annotations.commands

import me.sknz.ousubot.infrastructure.events.commands.autocomplete.CommandAutoComplete
import net.dv8tion.jda.api.interactions.commands.OptionType
import kotlin.reflect.KClass


/**
 * Anotação para declarar uma função/método como um SlashCommand
 * Ao declarar uma função como SlashCommand, ela começa a se comportar
 * como um comando que poderá ser chamado pelo bot.
 *
 * Se a classe onde este comando está, estiver com a propriedade [SlashCommandController.name]
 * este comando irá fazer parte de um comando como um subcomando.
 *
 * @see SlashCommandController
 * @see SlashCommand.Options
 */
@Retention(value = AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class SlashCommand(val name: String,
                              val description: String = "Not specified") {
    /**
     * Anotação utilizada para especificar uma opção de um comando ou subcomando
     *
     * Ela deve ser utilizada em conjunto com a [SlashCommand]
     * ```
     * @SlashCommand(name="hello", description="Hello World!")
     * @SlashCommand.Option(option=OptionType.BOOLEAN,
     *                     name="world?",
     *                     description="Say hello world or no?",
     *                     required=true)
     * fun myCommand() {}
     * ```
     *
     * @see SlashCommand.Options
     * @see SlashCommand
     */
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FUNCTION)
    annotation class Option(
        val type: OptionType = OptionType.STRING,
        val name: String,
        val description: String,
        val required: Boolean = false,
        val choices: Array<Choice> = [],
        /**
         *  WARNING: O parametro autocomplete só aceita classes que estendem a [CommandAutoComplete]
         */
        val autocomplete: KClass<out CommandAutoComplete<*>> = CommandAutoComplete::class
    ) {
        @Retention(AnnotationRetention.RUNTIME)
        @Target(AnnotationTarget.FUNCTION)
        annotation class Choice(
            val name: String,
            val value: String
        )
    }

    /**
     * Anotação utilizada para especificar opções de um comando ou subcomando
     *
     * Ela deve ser utilizada em conjunto com a [SlashCommand]
     * ```
     * @SlashCommand(name="character", description="Create a simple character!")
     * @SlashCommand.Options([
     *   SlashCommand.Option(name="eyes", description = "eyes color"),
     *   SlashCommand.Option(name="hair", description = "hair color")
     * ])
     * fun character() {}
     * ```
     *
     * @see SlashCommand.Option
     * @see SlashCommand
     */
    @Retention(AnnotationRetention.RUNTIME)
    @Target(AnnotationTarget.FUNCTION)
    annotation class Options(val value: Array<Option> = [])
}

@Retention(value = AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class MessageInteraction(val name: String)

@Retention(value = AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class UserInteraction(val name: String)

package me.sknz.ousubot.core.annotations

import me.sknz.ousubot.core.commands.autocomplete.CommandAutoComplete
import net.dv8tion.jda.api.interactions.commands.OptionType
import kotlin.reflect.KClass

/**
 * Anotação utilizada para especificar uma opção de um comando ou subcomando
 *
 * Ela deve ser utilizada em conjunto com a [SlashCommand]
 * ```
 * @SlashCommand(name="hello", description="Hello World!")
 * @SlashCommandOption(option=OptionType.BOOLEAN,
 *                     name="world?",
 *                     description="Say hello world or no?",
 *                     required=true)
 * fun myCommand() {}
 * ```
 *
 * @see SlashCommandOptions
 * @see SlashCommand
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class SlashCommandOption(
    val type: OptionType = OptionType.STRING,
    val name: String,
    val description: String,
    val required: Boolean = false,
    /**
     *  WARNING: O parametro autocomplete só aceita classes que estendem a [CommandAutoComplete]
     */
    val autocomplete: KClass<*> = Void::class
)

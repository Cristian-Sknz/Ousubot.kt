package me.sknz.ousubot.infrastructure.events.commands.custom

import me.sknz.ousubot.infrastructure.annotations.commands.SlashCommand
import me.sknz.ousubot.infrastructure.events.commands.autocomplete.CommandAutoComplete
import net.dv8tion.jda.api.interactions.commands.Command
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import org.springframework.context.ApplicationContext
import kotlin.reflect.KClass

/**
 * ## CustomOption
 *
 * Classe que estende [OptionData] e adiciona o paramêtro [complete]
 * para a utilização de [CommandAutoComplete].
 */
class CustomOption (
    option: OptionType,
    name: String,
    description: String,
    private val complete: KClass<out CommandAutoComplete<*>>? = null,
    required: Boolean = false
) : OptionData(option, name, description, required, complete != null) {

    fun setChoices(vararg choice: SlashCommand.Option.Choice): CustomOption {
        this.addChoices(choice.map { Command.Choice(it.name, it.value) })
        return this
    }

    fun getAutoComplete(context: ApplicationContext): CommandAutoComplete<*>? {
        complete?.let {
            return context.getBean(complete.java)
        }
        return null
    }
}
package me.sknz.ousubot.infrastructure.events.commands.custom

import me.sknz.ousubot.infrastructure.tools.InitOnceProperty.Companion.initOnce
import net.dv8tion.jda.api.interactions.commands.Command
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import kotlin.reflect.KFunction

/**
 * ## CustomSlashCommandData
 *
 * Classe customizada de [SlashCommandData] para comandos que serão
 * executados pelo bot.
 */
class CustomSlashCommandData: CommandDataImpl {

    var function: CallableCommand by initOnce()

    constructor(name: String, description: String = "no description"): super(name, description)
    constructor(type: Command.Type, name: String): super(type, name)

    private val subcommands: ArrayList<SlashSubcommandData> = arrayListOf()


    fun setFunction(instance: Any, function: KFunction<*>): CustomSlashCommandData {
        this.function = object : CallableCommand {
            override val function: KFunction<*> = function
            override val instance: Any = instance
            override val name: String = getName()
        }
        return this
    }

    fun getSubcommand(name: String): SlashSubcommandData? {
        return subcommands.find { it.name.equals(name, true) }
    }

    fun getOption(name: String): CustomOption? {
        return getOptions().find { it.name.equals(name, true) } as CustomOption?
    }

    override fun getSubcommands(): List<SubcommandData> = subcommands

    fun addSubcommand(subcommand: SubcommandData): CommandDataImpl {
        if (subcommand is SlashSubcommandData) {
            super.addSubcommands(subcommand)
            this.subcommands.add(subcommand)
            return this
        }
        throw IllegalArgumentException("${subcommand.name} não é um ${SlashSubcommandData::class.simpleName}")
    }

    fun addOptions(options: List<CustomOption>): CustomSlashCommandData {
        super.addOptions(options)
        return this
    }

    override fun addSubcommands(vararg subcommands: SubcommandData): CommandDataImpl {
        for (subcommand in subcommands) {
            addSubcommand(subcommand)
        }
        return this
    }

    /**
     * ## SlashSubcommandData
     *
     * Classe customizada de [SubcommandData] para comandos que serão
     * executados pelo bot.
     */
    class SlashSubcommandData(name: String, description: String) : SubcommandData(name, description) {
        var function: CallableCommand by initOnce()

        fun setFunction(instance: Any, function: KFunction<*>): SlashSubcommandData {
            this.function = object : CallableCommand {
                override val name: String = getName()
                override val function: KFunction<*> = function
                override val instance: Any = instance
            }
            return this
        }

        fun getOption(name: String): CustomOption? {
            return getOptions().find { it.name.equals(name, true) } as CustomOption?
        }

        fun addOptions(options: List<CustomOption>): SlashSubcommandData {
            super.addOptions(options)
            return this
        }
    }
}
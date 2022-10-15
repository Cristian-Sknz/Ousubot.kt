package me.sknz.ousubot.infrastructure.events.commands.custom

import net.dv8tion.jda.api.interactions.commands.Command
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import kotlin.reflect.KFunction

class CustomSlashCommandData: CommandDataImpl, RunnableCommand {


    constructor(name: String, description: String = "no description"): super(name, description)
    constructor(type: Command.Type, name: String): super(type, name)

    override lateinit var function: CommandFunction
    private val subcommands: ArrayList<SlashSubcommandData> = arrayListOf()

    fun addOptions(options: List<CustomOption>): CustomSlashCommandData {
        super.addOptions(options)
        return this
    }

    fun setFunction(instance: Any, function: KFunction<*>): CustomSlashCommandData {
        this.function = CommandFunction(instance, function)
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

    override fun addSubcommands(vararg subcommands: SubcommandData): CommandDataImpl {
        for (subcommand in subcommands) {
            addSubcommand(subcommand)
        }
        return this
    }

    class SlashSubcommandData(name: String, description: String)
        : SubcommandData(name, description), RunnableCommand {
        override lateinit var function: CommandFunction

        fun addOptions(options: List<CustomOption>): SlashSubcommandData {
            super.addOptions(options)
            return this
        }

        fun setFunction(instance: Any, function: KFunction<*>): SlashSubcommandData {
            this.function = CommandFunction(instance, function)
            return this
        }

        fun getOption(name: String): CustomOption? {
            return getOptions().find { it.name.equals(name, true) } as CustomOption?
        }
    }
}
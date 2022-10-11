package me.sknz.ousubot.core.commands.custom

import net.dv8tion.jda.api.interactions.commands.Command
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import net.dv8tion.jda.internal.interactions.CommandDataImpl
import kotlin.reflect.KFunction

class CustomSlashCommandData: CommandDataImpl, RunnableCommand {

    constructor(name: String, description: String = "no description"): super(name, description) {}
    constructor(type: Command.Type, name: String): super(type, name) {}

    override lateinit var function: CommandFunction

    fun addOptions(options: List<CustomOption>): CustomSlashCommandData {
        super.addOptions(options)
        return this
    }

    fun setFunction(instance: Any, function: KFunction<*>): CustomSlashCommandData {
        this.function = CommandFunction(instance, function)
        return this
    }

    fun getSubcommand(name: String): SlashSubcommandData? {
        return subcommands.find { it.name.equals(name, true) } as SlashSubcommandData?
    }

    fun getOption(name: String): CustomOption? {
        return getOptions().find { it.name.equals(name, true) } as CustomOption?
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
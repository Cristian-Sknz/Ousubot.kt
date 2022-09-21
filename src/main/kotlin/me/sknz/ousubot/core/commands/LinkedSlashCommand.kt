package me.sknz.ousubot.core.commands

import me.sknz.ousubot.core.commands.exception.CommandRegisterException
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import kotlin.reflect.KFunction

/**
 * LinkedSlashCommand é uma classe para armazenar comandos e subcomandos.
 * Apos um comando ser registrado com sucesso ele é transformado nessa classe
 * e poderá ser executado ao chamar o método [LinkedSlashCommand.getFunction]
 *
 * @see SlashCommands
 */
data class LinkedSlashCommand(
    val instance: Any,
    val command: Any,
    val subcommands: Collection<LinkedSlashCommand>?,
    val function: KFunction<*>?,
    val options: Collection<LinkedOption>?
) {

    init {
        if (command !is SlashCommandData && command !is SubcommandData) {
            throw CommandRegisterException("Este comando não é um SlashCommandData ou SubCommandData")
        }
    }

    fun getFunction(name: String?): KFunction<*>? {
        return getLinkedSlashCommand(name)?.function
    }

    fun getLinkedSlashCommand(name: String?): LinkedSlashCommand? {
        if (name == null) {
            if (subcommands !== null) {
                throw RuntimeException("Você não pode executar um comando sem especificar qual subcomando!")
            }
            return this
        }

        if (subcommands == null) {
            return this
        }

        return subcommands.find { it.getName().lowercase() == name }
    }

    fun getName(): String {
        return when (this.command) {
            is SlashCommandData -> this.command.name
            is SubcommandData -> this.command.name
            else -> throw RuntimeException("Não foi possível obter o nome de um comando!")
        }
    }
}
package me.sknz.ousubot.core.commands.exception

import me.sknz.ousubot.core.annotations.commands.SlashCommandController
import me.sknz.ousubot.core.annotations.commands.SlashCommand
import me.sknz.ousubot.core.commands.SlashCommands

/**
 * Exceção utilizada para declarar erros ao register um [SlashCommandController]
 *
 * @see SlashCommands
 * @see SlashCommand
 * @see SlashCommandController
 */
class CommandRegisterException(message: String?) : RuntimeException(message)
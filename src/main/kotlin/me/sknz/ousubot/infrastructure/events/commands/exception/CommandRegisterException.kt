package me.sknz.ousubot.infrastructure.events.commands.exception

import me.sknz.ousubot.infrastructure.annotations.commands.SlashCommandController
import me.sknz.ousubot.infrastructure.annotations.commands.SlashCommand
import me.sknz.ousubot.infrastructure.events.commands.SlashCommands

/**
 * Exceção utilizada para declarar erros ao register um [SlashCommandController]
 *
 * @see SlashCommands
 * @see SlashCommand
 * @see SlashCommandController
 */
class CommandRegisterException(message: String?) : RuntimeException(message)
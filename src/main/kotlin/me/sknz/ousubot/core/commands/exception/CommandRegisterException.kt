package me.sknz.ousubot.core.commands.exception

/**
 * Exceção utilizada para declarar erros ao register um [me.sknz.ousubot.core.annotations.SlashCommandController]
 *
 * @see me.sknz.ousubot.core.commands.SlashCommands
 * @see me.sknz.ousubot.core.annotations.SlashCommand
 * @see me.sknz.ousubot.core.annotations.SlashCommandController
 */
class CommandRegisterException(message: String?) : RuntimeException(message) {
}
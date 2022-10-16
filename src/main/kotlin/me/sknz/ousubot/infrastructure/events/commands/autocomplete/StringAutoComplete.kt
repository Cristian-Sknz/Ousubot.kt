package me.sknz.ousubot.infrastructure.events.commands.autocomplete

/**
 * ## StringAutoComplete
 *
 * ‘Interface’ para implementação de auto-completáveis para comandos.
 *
 * Estes auto-completes serão mostrados aos usuários conforme o que eles digitam.
 * Apenas os tipos [String] são compatíveis com esta implementação
 *
 * @see CommandAutoComplete
 */
interface StringAutoComplete: CommandAutoComplete<String>
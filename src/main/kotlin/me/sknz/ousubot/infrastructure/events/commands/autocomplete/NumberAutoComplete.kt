package me.sknz.ousubot.infrastructure.events.commands.autocomplete

/**
 * ## NumberAutoComplete
 *
 * ‘Interface’ para implementação de auto-completáveis para comandos.
 *
 * Estes auto-completes serão mostrados aos usuários conforme o que eles digitam.
 * Apenas os tipos [Number] são compatíveis com esta implementação
 *
 * @see CommandAutoComplete
 */
interface NumberAutoComplete: CommandAutoComplete<Number>
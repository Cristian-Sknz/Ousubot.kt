package me.sknz.ousubot.infrastructure.events.commands.autocomplete

/**
 * ‘Interface’ para implementação de auto-completáveis para comandos.
 *
 * Estes auto-completes serão mostrados aos usuários conforme o que eles digitam.
 * Apenas os tipos [String] e [Number] são compatíveis com esta implementação
 *
 * @see StringAutoComplete
 * @see NumberAutoComplete
 */
sealed interface CommandAutoComplete<T> {
    fun onAutoComplete(query: String?): Collection<CommandChoice<T>>
}
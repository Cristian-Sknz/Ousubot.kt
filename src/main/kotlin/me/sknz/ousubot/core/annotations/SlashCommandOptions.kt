package me.sknz.ousubot.core.annotations

/**
 * Anotação utilizada para especificar opções de um comando ou subcomando
 *
 * Ela deve ser utilizada em conjunto com a [SlashCommand]
 * ```
 * @SlashCommand(name="character", description="Create a simple character!")
 * @SlashCommandOptions([
 *   SlashCommandOption(name="eyes", description = "eyes color"),
 *   SlashCommandOption(name="hair", description = "hair color")
 * ])
 * fun character() {}
 * ```
 *
 * @see SlashCommandOption
 * @see SlashCommand
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class SlashCommandOptions(val value: Array<SlashCommandOption> = [])

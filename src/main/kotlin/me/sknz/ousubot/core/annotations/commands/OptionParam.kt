package me.sknz.ousubot.core.annotations.commands

/**
 * Anotação para indicar que um parâmetro irá receber uma
 * opção de um [SlashCommand].
 *
 * Se a opção não for valida ou não for preenchida,
 * o valor retornado no parâmetro será nulo.
 *
 * ```
 * class MyAwesomeCommand {
 *
 *      @SlashCommand(name="hello", description="Hello Command")
 *      @SlashCommandOption(
 *          type=OptionType.STRING,
 *          name="name",
 *          description="your friend's name")
 *      fun subcommand(@OptionParam("name") name: OptionMapping?) {}
 * }
 * ```
 * @param value nome da opção
 *
 * @see net.dv8tion.jda.api.interactions.commands.OptionMapping
 * @see SlashCommandOption
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class OptionParam(
    val value: String
)

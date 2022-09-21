package me.sknz.ousubot.core.annotations.commands

import org.springframework.context.ApplicationContext

/**
 * Anotação para indicar que um parâmetro irá receber uma
 * bean do Contexto do Spring.
 *
 * Se não for declarado nenhum valor em [FromBean.value],
 * ele irá pegar o primeiro candidato em [ApplicationContext.getBean]
 *
 * ```
 * class MyAwesomeCommand {
 *
 *      @SlashCommand(name="hello", description="Hello Command")
 *      fun subcommand(@FromBean mapper: ObjectMapper) {}
 * }
 * ```
 * @param value nome da bean
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class FromBean(
    val value: String = ""
)

package me.sknz.ousubot.core.commands.tools

import me.sknz.ousubot.core.annotations.commands.FromBean
import me.sknz.ousubot.core.annotations.commands.OptionParam
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.CommandInteraction
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction
import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.createType
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSuperclassOf
import kotlin.reflect.full.isSupertypeOf

/**
 * ## CommandParameterInjector
 *
 * [CommandParameterInjector] é uma classe responsável por injetar
 * dependências em funções que executam uma interação com [me.sknz.ousubot.core.annotations.commands.SlashCommand]
 *
 * @see me.sknz.ousubot.core.annotations.commands.SlashCommand
 * @see me.sknz.ousubot.core.commands.SlashCommands
 */
class CommandParameterInjector(
    private val context: ApplicationContext
) {

    companion object {
        /**
         * Array com parâmetros validos utilizados em
         * [CommandParameterInjector.getInitializedParameters]
         */
        val VALID_PARAMETERS = arrayOf(
            SlashCommandInteractionEvent::class,
            SlashCommandInteraction::class,
            CommandInteraction::class,
            OptionMapping::class,
            User::class
        )
    }

    /**
     * Função que inicializa os parâmetros necessários para a
     * execução de uma função.
     *
     * Parâmetros inválidos sempre vão retornar 'null'.
     *
     * @param event Evento em que a função se encontra
     * @param function Função em que os parâmetros serão injetados posteriormente
     */
    fun getInitializedParameters(event: GenericCommandInteractionEvent,
                                 function: KFunction<*>): List<Any?> {
        val parameters = function.parameters.toMutableList()
        parameters.removeFirst()

        val values = parameters.map { parameter ->
            val clazz = VALID_PARAMETERS.find { valid ->
                parameter.type.isSupertypeOf(valid.createType())
            }

            if (clazz === null) {
                val annotation = parameter.findAnnotation<FromBean>()
                    ?: return@map null

                return@map context.getBean(parameter, annotation)
            }

            when {
                clazz.isSuperclassOf(SlashCommandInteractionEvent::class) -> event
                clazz.isSuperclassOf(SlashCommandInteraction::class) -> event.interaction
                clazz.isSuperclassOf(CommandInteraction::class) -> event
                clazz.isSuperclassOf(OptionMapping::class) -> {
                    parameter.findAnnotation<OptionParam>()?.let {
                        event.getOption(it.value)
                    }
                }
                clazz.isSuperclassOf(User::class) -> event.user
                else -> null
            }
        }
        return values
    }

    /**
     * Função simples para pegar a bean da anotação [FromBean];
     *
     * Retorna null se a função for um vararg([KParameter.isVararg])
     * ou se uma [BeansException] for disparada.
     */
    private fun ApplicationContext.getBean(parameter: KParameter, annotation: FromBean): Any? {
        if (parameter.isVararg) return null
        try {
            val clazz = (parameter.type.classifier as KClass<*>).java
            if (annotation.value.isEmpty()) {
                return this.getBean(clazz)
            }
            return this.getBean(annotation.value, clazz)
        } catch (exception: BeansException) {
            return null
        }
    }
}
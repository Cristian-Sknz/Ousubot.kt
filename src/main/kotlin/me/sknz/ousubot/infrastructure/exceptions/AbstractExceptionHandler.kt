package me.sknz.ousubot.infrastructure.exceptions

import me.sknz.ousubot.infrastructure.annotations.ExceptionHandler
import net.dv8tion.jda.api.events.GenericEvent
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubclassOf

/**
 * ## AbstractExceptionHandler
 *
 * Uma classe abstrata que serve como um "boilerplate" para criar
 * um controlador de exceções.
 */
abstract class AbstractExceptionHandler {

    /**
     * Funções que controlam as exceções
     */
    private val functions: HashMap<KClass<out Throwable>, KFunction<*>> = hashMapOf()

    /**
     * Ao instanciar a classe, pegar todas as funções anotadas com [ExceptionHandler]
     * e armazenar em um [HashMap].
     */
    init {
        for (value in this::class.declaredFunctions) {
            val annotation = value.findAnnotation<ExceptionHandler>()?: continue
            functions[annotation.value] = value
        }
    }

    /**
     * Função que pode ser chamada para iniciar uma sequência de exceções,
     * executando todas as funções anotadas com [ExceptionHandler].
     *
     * @param event Evento do JDA
     * @param throwable Exceção disparada.
     */
    open fun onException(event: GenericEvent?, throwable: Throwable) {
        for ((key, value) in functions) {
            if (throwable::class === key) execute(value, event, throwable)
        }
    }

    /**
     * Função que irá executar uma função anotada com [ExceptionHandler]
     * e irá injetar todos os parâmetros validos dela.
     *
     * @param function função anotada com [ExceptionHandler]
     * @param args parâmetros validos
     *
     * @throws NullPointerException caso um parâmetro obrigatório seja nulo.
     */
    private fun execute(function: KFunction<*>, vararg args: Any?) {
        val parameters = function.parameters.map { parameter ->
            (parameter.type.classifier as KClass<*>).let {
                args.filterNotNull().find { argument -> argument::class.isSubclassOf(it) }
            }
        }.toMutableList()
        parameters.removeFirst()
        function.call(this, *parameters.toTypedArray())
    }
}
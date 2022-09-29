package me.sknz.ousubot.core.exceptions

import me.sknz.ousubot.core.annotations.ExceptionHandler
import net.dv8tion.jda.api.events.GenericEvent
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.isSubclassOf

abstract class AbstractExceptionHandler {

    private val functions: HashMap<KClass<out Throwable>, KFunction<*>> = hashMapOf()

    init {
        for (value in this::class.declaredFunctions) {
            val annotation = value.findAnnotation<ExceptionHandler>()?: continue
            functions[annotation.value] = value
        }
    }

    fun onException(event: GenericEvent?, throwable: Throwable) {
        for ((key, value) in functions) {
            if (throwable::class === key) execute(value, event, throwable)
        }
    }

    private fun execute(function: KFunction<*>, vararg args: Any?) {
        val parameters = function.parameters.map { parameter ->
            (parameter.type.classifier as KClass<*>).let {
                args.filterNotNull().find { argument -> it.isSubclassOf(argument::class)  }
            }
        }.toMutableList()
        parameters.removeFirst()
        function.call(this, *parameters.toTypedArray())
    }

}
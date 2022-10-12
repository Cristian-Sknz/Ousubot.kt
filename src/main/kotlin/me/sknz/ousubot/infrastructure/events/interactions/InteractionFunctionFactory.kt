package me.sknz.ousubot.infrastructure.events.interactions

import me.sknz.ousubot.infrastructure.annotations.interaction.InteractionController
import me.sknz.ousubot.infrastructure.annotations.interaction.InteractionHandler
import me.sknz.ousubot.infrastructure.events.InteractionFactory
import me.sknz.ousubot.infrastructure.events.interactions.exception.InteractionRegisterException
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

class InteractionFunctionFactory : InteractionFactory<LinkedInteraction> {

    override fun create(instance: Any, clazz: KClass<out Any>, vararg params: Any?): List<LinkedInteraction> {
        val controller = clazz.findAnnotation<InteractionController>()
            ?: throw InteractionRegisterException("A Classe ${clazz.simpleName} não foi declarada como uma InteractionController")

        val type = params.find { it is KClass<*> }!!
        val functions = clazz.declaredFunctions.filter {
            it.hasAnnotation<InteractionHandler>()
        }

        if (controller.type.clazz !== type) {
            throw InteractionRegisterException("A interação do tipo ${controller.type} está incorreta em ${this::class.simpleName}!")
        }

        if (functions.isEmpty()) {
            throw InteractionRegisterException("Não foram encontrados manipuladores de ${type.simpleName} anotados com @InteractionHandler em ${clazz.simpleName})")
        }

        if (controller.id.contains(":")) {
            throw InteractionRegisterException("A classe '${clazz.simpleName}' não tem um identificador valido.")
        }

        val values = createInterations(instance, functions)
        if (controller.id.isEmpty()) {
            return values
        }

        return listOf(createInteractionHandlerGroup(instance, controller.id, values))
    }

    private fun createInterations(instance: Any, functions: Collection<KFunction<*>>): List<LinkedInteraction> {
        return functions.map {
            val annotation = it.findAnnotation<InteractionHandler>()!!
            return@map createInteractionHandler(instance, annotation.id, annotation.idType, it)
        }
    }

    private fun createInteractionHandler(instance: Any, id: String, type: IDType, function: KFunction<*>): LinkedInteraction {
        if (id.isEmpty() || id.contains(":")) {
            throw InteractionRegisterException("Função '${instance::class.simpleName}#${function.name}' não possui identificador valido!")
        }
        return LinkedInteraction(instance, id, type, function)
    }

    private fun createInteractionHandlerGroup(instance: Any, id: String, interactions: Collection<LinkedInteraction>): LinkedInteraction {
        return LinkedInteraction(instance, id, interactions)
    }
}
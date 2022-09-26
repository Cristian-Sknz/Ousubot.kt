package me.sknz.ousubot.core.interactions

import me.sknz.ousubot.core.annotations.interaction.InteractionController
import me.sknz.ousubot.core.annotations.interaction.InteractionHandler
import me.sknz.ousubot.core.interactions.exception.InteractionRegisterException
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.interactions.Interaction
import net.dv8tion.jda.api.requests.RestAction
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.util.function.Function
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf

/**
 * AbstractInteraction é uma classe abstrata responsável em manipular, armazenar e registrar interações.
 * Está classe é uma classe interna e não deve ser modificada externamente!
 *
 * Essa classe é chamada ao configurar o [net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder]
 * em [me.sknz.ousubot.core.JDAConfiguration]
 *
 * @see InteractionController
 * @see InteractionHandler
 */
abstract class AbstractInteraction<T : Interaction>(
    private val type: KClass<T>,
    private val interactionId: Function<GenericEvent, String?>
) {

    protected val interactions: HashMap<String, LinkedInteraction> = hashMapOf()
    protected val logger: Logger = LogManager.getLogger(this::class.java)

    open fun onGenericInteraction(event: GenericEvent) {
        if (!event::class.isSubclassOf(type)) {
            return
        }

        val fullId = interactionId.apply(event)
        val id = fullId?.split(":")
        if (id.isNullOrEmpty()) {
            return
        }

        if (id.size > 1) {
            val modal = interactions.values.find { it.id.equals(id[0], true) } ?: return
            modal.execute(fullId, event)?.let {
                if (it is RestAction<*>) it.queue()
            }
            return
        }

        val modal = interactions.values.find { it.id.equals(id[0], true) } ?: return
        modal.execute(fullId, event)?.let {
            if (it is RestAction<*>) it.queue()
        }
    }

    /**
     * Função para registrar interação(ações) de uma instância anotada com [InteractionController].
     * Está função por baixo dos panos, utiliza reflexão para manipular as anotações
     *
     * @param instance Uma classe instanciada de [InteractionController]
     * @see InteractionController
     */
    fun register(instance: Any) {
        val clazz = instance::class
        val controller = clazz.findAnnotation<InteractionController>()
            ?: throw InteractionRegisterException("A Classe ${clazz.simpleName} não foi declarada como uma InteractionController")

        val functions = clazz.declaredFunctions.filter {
            it.hasAnnotation<InteractionHandler>()
        }

        if (controller.type.clazz !== type) {
            throw InteractionRegisterException("A interação do tipo ${controller.type} está incorreta em ${this::class.simpleName}!")
        }

        if (functions.isEmpty()) {
            logger.warn("Não foram encontrados manipuladores de ${type.simpleName} anotados com @InteractionHandler em ${clazz.simpleName}")
            return
        }

        if (controller.id.contains(":")) {
            logger.warn("A Classe '${clazz.simpleName}' não foi registrada, pois não tem um identificador valido!")
            return
        }

        val values = createInterations(instance, functions)
        if (controller.id.isEmpty()) {
            values.forEach {
                interactions[it.id] = it
            }
            logger.info("Foram registrados ${values.size} interações de ${type.simpleName}.")
            return
        }

        interactions[controller.id.lowercase()] = createInteractionHandlerGroup(instance, controller.id, values)
        logger.info("Foram registrados ${values.size} interações do grupo de interações '${controller.id}'.")
    }

    protected fun createInterations(instance: Any, functions: Collection<KFunction<*>>): List<LinkedInteraction> {
        return functions.map {
            val annotation = it.findAnnotation<InteractionHandler>()!!
            return@map createInteractionHandler(instance, annotation.id, annotation.idType, it)
        }.filterNotNull()
    }

    protected fun createInteractionHandler(instance: Any, id: String, type: IDType, function: KFunction<*>): LinkedInteraction? {
        if (id.isEmpty() || id.contains(":")) {
            logger.warn("Função '${function.name}' não foi registrada, pois não tem um identificador valido!")
            return null
        }
        return LinkedInteraction(instance, id, type, function)
    }

    protected fun createInteractionHandlerGroup(instance: Any, id: String, interactions: Collection<LinkedInteraction>): LinkedInteraction {
        return LinkedInteraction(instance, id, interactions)
    }
}
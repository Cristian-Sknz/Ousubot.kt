package me.sknz.ousubot.infrastructure.events.interactions

import me.sknz.ousubot.infrastructure.annotations.interaction.InteractionController
import me.sknz.ousubot.infrastructure.annotations.interaction.InteractionHandler
import me.sknz.ousubot.infrastructure.events.interactions.exception.InteractionRegisterException
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent
import net.dv8tion.jda.api.requests.RestAction
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * AbstractInteraction é uma classe abstrata responsável em manipular, armazenar e registrar interações.
 * Está classe é uma classe interna e não deve ser modificada externamente!
 *
 * Essa classe é chamada ao configurar o [net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder]
 * em [me.sknz.ousubot.infrastructure.JDAConfiguration]
 *
 * @see InteractionController
 * @see InteractionHandler
 */
abstract class AbstractInteraction<T : GenericInteractionCreateEvent>(
    private val type: KClass<T>,
) {

    private val factory = InteractionFunctionFactory()
    protected val interactions: HashMap<String, LinkedInteraction> = hashMapOf()
    private val logger: Logger = LogManager.getLogger(this::class.java)

    abstract fun getInteractionId(event: GenericEvent): String?

    open fun onGenericInteraction(event: GenericEvent) {
        if (!event::class.isSubclassOf(type)) {
            return
        }
        val fullId = getInteractionId(event)
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
     * @throws InteractionRegisterException ao encontrar problemas em interações
     * @see InteractionController
     */
    fun register(instance: Any) {
        val interactions = factory.create(instance, instance::class, type)
        interactions.forEach {
            this.interactions[it.id] = it
        }
        logger.info("Foram registrados ${interactions.sumOf { it.group.size.coerceAtLeast(1) }} interações em ${instance::class.simpleName}")
    }
}
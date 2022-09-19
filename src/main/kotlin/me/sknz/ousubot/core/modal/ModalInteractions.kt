package me.sknz.ousubot.core.modal

import me.sknz.ousubot.core.annotations.commands.SlashCommandController
import me.sknz.ousubot.core.annotations.modal.ModalController
import me.sknz.ousubot.core.annotations.modal.ModalHandler
import me.sknz.ousubot.core.modal.exception.ModalRegisterException
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.hooks.SubscribeEvent
import net.dv8tion.jda.api.requests.RestAction
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

/**
 * ModalInteractions é responsável em armazenar e registrar interações com modais.
 * Está classe é uma classe interna e não deve ser modificada externamente!
 *
 * Essa classe é chamada ao configurar o [net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder]
 * em [me.sknz.ousubot.core.JDAConfiguration]
 *
 * @see ModalController
 * @see ModalHandler
 */
internal class ModalInteractions {

    private val map: HashMap<String, LinkedModalInteraction> = hashMapOf()
    private val logger: Logger = LogManager.getLogger(ModalInteractions::javaClass)

    @SubscribeEvent
    fun onModalInteract(event: ModalInteractionEvent) {
        val id = event.modalId.split(":")
        if (id.size > 1) {
            val modal = map.values.find { it.id.equals(id[0], true) } ?: return
            modal.execute(event.modalId, event)?.let {
                if (it is RestAction<*>) it.queue()
            }
            return
        }

        val modal = map.values.find { it.id.equals(id[0], true) } ?: return
        modal.execute(event.modalId, event)?.let {
            if (it is RestAction<*>) it.queue()
        }
    }

    /**
     * Função para registrar interação(ações) de uma instância de um [SlashCommandController].
     * Está função por baixo dos panos, utiliza reflexão para manipular as anotações
     *
     * @param instance Uma classe instanciada de [ModalController]
     * @see ModalController
     */
    fun register(instance: Any) {
        val clazz = instance::class
        val controller = clazz.findAnnotation<ModalController>()
            ?: throw ModalRegisterException("A Classe ${clazz.simpleName} não foi declarada como um ModalController")

        val functions = clazz.declaredFunctions.filter {
            it.hasAnnotation<ModalHandler>()
        }

        if (functions.isEmpty()) {
            logger.warn("Não foram encontrados manipuladores de modal anotados com @ModalHandler em ${clazz.simpleName}")
            return
        }

        if (controller.id.isEmpty()) {
            val values = createLinkedModalInteractions(instance, functions)
            values.forEach {
                map[it.id] = it
            }
            logger.info("Foram registrados ${values.size} interações de modal.")
            return
        }

        if (controller.id.contains(":")) {
            logger.warn("A Classe '${clazz.simpleName}' não foi registrada, pois não tem um identificador valido!")
            return
        }

        val values = createLinkedModalInteractions(instance, functions)
        map[controller.id.lowercase()] = LinkedModalInteraction(instance, controller.id, values)
        logger.info("Foram registrados ${values.size} interações do grupo de modal '${controller.id}'.")
    }

    private fun createLinkedModalInteractions(instance: Any, functions: Collection<KFunction<*>>): ArrayList<LinkedModalInteraction> {
        val results = arrayListOf<LinkedModalInteraction>()
        for (function in functions) {
            val modal = function.findAnnotation<ModalHandler>()!!
            if (modal.id.isEmpty() || modal.id.contains(":")) {
                logger.warn("Função '${function.name}' não foi registrada, pois não tem um identificador valido!")
                continue
            }
            results.add(LinkedModalInteraction(instance, modal.id, modal.idType,  function))
        }
        return results
    }
}
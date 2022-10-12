package me.sknz.ousubot.infrastructure.events.interactions

import kotlin.reflect.KFunction
import me.sknz.ousubot.infrastructure.events.interactions.exception.InvalidInteractionException
import me.sknz.ousubot.infrastructure.annotations.interaction.InteractionHandler

/**
 * LinkedInteraction é uma classe responsável em armazenar interações com botões, modais entre outros.
 * Apos uma interação com modal for registrada ela é transformada nessa classe
 * e poderá ser executado ao chamar o método [LinkedInteraction.execute]
 *
 * @see InteractionHandler
 */
data class LinkedInteraction(
    val instance: Any,
    val id: String,
    val type: IDType = IDType.EQUALS,
    val group: Collection<LinkedInteraction> = emptyList(),
    val function: KFunction<*>?,
) {

    constructor(instance: Any, id: String, group: Collection<LinkedInteraction>) :
            this(instance, id, IDType.EQUALS, group, null)

    constructor(instance: Any, id: String, type: IDType, function: KFunction<*>?):
            this(instance, id, type, emptyList(), function)

    fun execute(name: String, vararg args: Any?): Any? {
        val split = name.split(":")
        if (split.size > 1) {
            val modal = getFromGroup(split[1]) ?:
                throw InvalidInteractionException("A interação '$name' não existe em '$id'")

            return modal.function?.call(instance, *args)
        }

        if (type.isValidId(id, split[0])) {
            return function?.call(instance, *args)
        }
        throw InvalidInteractionException("A interação '$name' não é valida em '$id'")
    }

    private fun getFromGroup(value: String): LinkedInteraction? {
        return group.find { it.type.isValidId(it.id, value)  }
    }
}

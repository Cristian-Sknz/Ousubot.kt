package me.sknz.ousubot.core.modal

import kotlin.reflect.KFunction
import me.sknz.ousubot.core.annotations.modal.ModalHandler
import me.sknz.ousubot.core.modal.exception.InvalidModalInteraction

/**
 * LinkedModalInteraction é uma classe responsável em armazenar interações com modais.
 * Apos uma interação com modal for registrada ela é transformada nessa classe
 * e poderá ser executado ao chamar o método [LinkedModalInteraction.execute]
 *
 * @see ModalHandler
 */
data class LinkedModalInteraction(
    val instance: Any,
    val id: String,
    val type: IDType = IDType.EQUALS,
    val group: Collection<LinkedModalInteraction> = emptyList(),
    val function: KFunction<*>?,
) {

    constructor(instance: Any, id: String, group: Collection<LinkedModalInteraction>) :
            this(instance, id, IDType.EQUALS, group, null)

    constructor(instance: Any, id: String, type: IDType, function: KFunction<*>?):
            this(instance, id, type, emptyList(), function)

    fun execute(name: String, vararg args: Any?): Any? {
        val split = name.split(":")
        if (split.size > 1) {
            val modal = getFromGroup(split[1]) ?:
                throw InvalidModalInteraction("A interação '$name' não existe em '$id'")

            return modal.function?.call(instance, *args)
        }

        if (type.isValidId(id, split[0])) {
            return function?.call(instance, *args)
        }
        throw InvalidModalInteraction("A interação '$name' não é valida em '$id'")
    }

    private fun getFromGroup(value: String): LinkedModalInteraction? {
        return group.find { it.type.isValidId(it.id, value)  }
    }
}

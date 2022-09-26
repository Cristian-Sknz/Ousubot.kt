package me.sknz.ousubot.core.annotations.interaction

import me.sknz.ousubot.core.interactions.IDType

/**
 * Anotação para declarar uma função como uma interação.
 * Ao declarar uma função como ModalHandler, ela começa a se comportar
 * como um manipulador de interações que poderá ser chamado pelo bot.
 *
 * Se a classe onde este comando está, estiver com a propriedade [InteractionController.id]
 * este evento irá fazer parte de um grupo de manipuladores de interação.
 *
 * @see InteractionController
 */
@Retention(value = AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class InteractionHandler(
    val idType: IDType = IDType.EQUALS,
    val id: String
)

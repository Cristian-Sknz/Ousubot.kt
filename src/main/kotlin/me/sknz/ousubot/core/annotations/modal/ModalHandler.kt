package me.sknz.ousubot.core.annotations.modal

import me.sknz.ousubot.core.modal.IDType

/**
 * Anotação para declarar uma função/método como uma interação a modal
 * Ao declarar uma função como ModalHandler, ela começa a se comportar
 * como um evento de modal que poderá ser chamado pelo bo t.
 *
 * Se a classe onde este comando está, estiver com a propriedade [ModalController.id]
 * este evento irá fazer parte de um grupo de eventos de modal.
 *
 * @see ModalController
 */
@Retention(value = AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ModalHandler(
    val idType: IDType = IDType.EQUALS,
    val id: String
)

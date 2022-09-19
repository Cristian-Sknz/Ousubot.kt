package me.sknz.ousubot.core.modal.exception

import me.sknz.ousubot.core.annotations.modal.ModalController
import me.sknz.ousubot.core.annotations.modal.ModalHandler
import me.sknz.ousubot.core.modal.ModalInteractions

/**
 * Exceção utilizada para declarar erros ao encontrar um grupo de interações,
 * mas a interação não é valida.
 *
 * @see ModalHandler
 * @see ModalController
 * @see ModalInteractions
 */
class InvalidModalInteraction(message: String?): RuntimeException(message)
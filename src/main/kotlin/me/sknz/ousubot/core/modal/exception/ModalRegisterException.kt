package me.sknz.ousubot.core.modal.exception

import me.sknz.ousubot.core.annotations.modal.ModalController
import me.sknz.ousubot.core.annotations.modal.ModalHandler
import me.sknz.ousubot.core.modal.ModalInteractions
/**
 * Exceção utilizada para declarar erros ao register um [ModalController]
 *
 * @see ModalHandler
 * @see ModalController
 * @see ModalInteractions
 */
class ModalRegisterException(message: String?): RuntimeException(message)
package me.sknz.ousubot.infrastructure.events.interactions.exception

/**
 * Exceção utilizada para declarar erros ao encontrar um grupo de interações,
 * mas a interação não é valida.
 */
class InvalidInteractionException(message: String?): RuntimeException(message)
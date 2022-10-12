package me.sknz.ousubot.infrastructure.events.interactions.exception

import me.sknz.ousubot.infrastructure.annotations.interaction.InteractionHandler
import me.sknz.ousubot.infrastructure.annotations.interaction.InteractionController
import me.sknz.ousubot.infrastructure.events.interactions.AbstractInteraction
/**
 * Exceção utilizada para declarar erros ao register um [InteractionController]
 *
 * @see InteractionHandler
 * @see InteractionController
 * @see AbstractInteraction
 */
class InteractionRegisterException(message: String?): RuntimeException(message)
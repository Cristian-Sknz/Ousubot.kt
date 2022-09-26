package me.sknz.ousubot.core.interactions.exception

import me.sknz.ousubot.core.annotations.interaction.InteractionHandler
import me.sknz.ousubot.core.annotations.interaction.InteractionController
import me.sknz.ousubot.core.interactions.AbstractInteraction
/**
 * Exceção utilizada para declarar erros ao register um [InteractionController]
 *
 * @see InteractionHandler
 * @see InteractionController
 * @see AbstractInteraction
 */
class InteractionRegisterException(message: String?): RuntimeException(message)
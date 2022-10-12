package me.sknz.ousubot.infrastructure.annotations.interaction

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import kotlin.reflect.KClass

enum class InteractionType(val clazz: KClass<*>) {
    MODAL(ModalInteractionEvent::class),
    BUTTON(ButtonInteractionEvent::class)
}
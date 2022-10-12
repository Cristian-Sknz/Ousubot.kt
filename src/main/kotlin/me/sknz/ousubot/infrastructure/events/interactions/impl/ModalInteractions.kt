package me.sknz.ousubot.infrastructure.events.interactions.impl

import me.sknz.ousubot.infrastructure.events.interactions.AbstractInteraction
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.hooks.SubscribeEvent

/**
 * ModalInteractions é responsável em armazenar e registrar interações com modais.
 * Está classe é uma classe interna e não deve ser modificada externamente!
 *
 * Essa classe é chamada ao configurar o [net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder]
 * em [me.sknz.ousubot.infrastructure.JDAConfiguration]
 *
 * @see AbstractInteraction
 */
class ModalInteractions : AbstractInteraction<ModalInteractionEvent>(ModalInteractionEvent::class){
    override fun getInteractionId(event: GenericEvent): String? {
        if (event is ModalInteractionEvent) {
            return event.modalId
        }
        return null
    }

    @SubscribeEvent
    override fun onGenericInteraction(event: GenericEvent) {
        super.onGenericInteraction(event)
    }
}
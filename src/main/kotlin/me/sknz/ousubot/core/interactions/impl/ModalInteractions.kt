package me.sknz.ousubot.core.interactions.impl

import me.sknz.ousubot.core.interactions.AbstractInteraction
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent
import net.dv8tion.jda.api.hooks.SubscribeEvent

/**
 * ModalInteractions é responsável em armazenar e registrar interações com modais.
 * Está classe é uma classe interna e não deve ser modificada externamente!
 *
 * Essa classe é chamada ao configurar o [net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder]
 * em [me.sknz.ousubot.core.JDAConfiguration]
 *
 * @see AbstractInteraction
 */
class ModalInteractions : AbstractInteraction<ModalInteractionEvent>(ModalInteractionEvent::class, {
    if (it is ModalInteractionEvent) {
        it.modalId
    } else null
}){

    @SubscribeEvent
    override fun onGenericInteraction(event: GenericEvent) {
        super.onGenericInteraction(event)
    }
}
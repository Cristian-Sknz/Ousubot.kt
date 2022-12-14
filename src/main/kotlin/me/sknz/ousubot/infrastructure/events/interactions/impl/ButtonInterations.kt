package me.sknz.ousubot.infrastructure.events.interactions.impl

import me.sknz.ousubot.infrastructure.events.interactions.AbstractInteraction
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.SubscribeEvent
import java.time.OffsetDateTime

/**
 * ButtonInterations é responsável em armazenar e registrar interações com botões.
 * Está classe é uma classe interna e não deve ser modificada externamente!
 *
 * Essa classe é chamada ao configurar o [net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder]
 * em [me.sknz.ousubot.infrastructure.JDAConfiguration]
 *
 * @see AbstractInteraction
 */
class ButtonInterations : AbstractInteraction<ButtonInteractionEvent>(ButtonInteractionEvent::class) {

    override fun getInteractionId(event: GenericEvent): String? {
        if (event is ButtonInteractionEvent) {
            return event.componentId
        }
        return null
    }

    @SubscribeEvent
    override fun onGenericInteraction(event: GenericEvent) {
        if (event is ButtonInteractionEvent) {
            /**
             * Verificação para ver se a interação expirou
             */
            if (OffsetDateTime.now().isAfter(event.message.timeCreated.plusMinutes(5))) {
                return
            }
        }

        super.onGenericInteraction(event)
    }
}
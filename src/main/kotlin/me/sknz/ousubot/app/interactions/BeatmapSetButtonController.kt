package me.sknz.ousubot.app.interactions

import me.sknz.ousubot.domain.dto.BeatmapSetRequest
import me.sknz.ousubot.infrastructure.annotations.interaction.InteractionController
import me.sknz.ousubot.infrastructure.annotations.interaction.InteractionHandler
import me.sknz.ousubot.infrastructure.annotations.interaction.InteractionType
import me.sknz.ousubot.infrastructure.events.interactions.IDType
import me.sknz.ousubot.domain.services.impl.BeatmapServiceImpl
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.requests.RestAction

@InteractionController(type = InteractionType.BUTTON, id = "beatmapset")
class BeatmapSetButtonController(
    val service: BeatmapServiceImpl
) {

    companion object {
        const val BEATMAPSET_CHANGE = "beatmapset:change:"
    }

    @InteractionHandler(id = "change", idType = IDType.STARTS_WITH)
    fun change(event: ButtonInteractionEvent): RestAction<*> {
        val ids = event.getBeatmapIds()
        val embed = service.getBeatmapEmbed(BeatmapSetRequest(ids[0], ids[1], event.userLocale))

        val buttons = event.message.buttons.map {
            if (it.id == null) return@map it
            if (it.label.contains("back", true)) {
                return@map it.withId("$BEATMAPSET_CHANGE${embed.payload.beatmapSetId}-${embed.back}")
                    .withDisabled(!embed.hasBack())
            }
            if (it.label.contains("next", true)) {
                return@map it.withId("$BEATMAPSET_CHANGE${embed.payload.beatmapSetId}-${embed.next}")
                    .withDisabled(!embed.hasNext())
            }
            return@map it
        }
        return event.editMessageEmbeds(embed.toMessageEmbed()).setActionRow(buttons)
    }

    private fun ButtonInteractionEvent.getBeatmapIds(): List<Int> {
        return this.componentId.split(":")[2].split("-").map { it.toInt() }
    }
}
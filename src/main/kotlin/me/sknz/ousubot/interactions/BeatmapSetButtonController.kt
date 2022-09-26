package me.sknz.ousubot.interactions

import me.sknz.ousubot.core.annotations.interaction.InteractionController
import me.sknz.ousubot.core.annotations.interaction.InteractionHandler
import me.sknz.ousubot.core.annotations.interaction.InteractionType
import me.sknz.ousubot.core.interactions.IDType
import me.sknz.ousubot.services.BeatmapService
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.requests.RestAction

@InteractionController(type = InteractionType.BUTTON, id = "beatmapset")
class BeatmapSetButtonController(
    val service: BeatmapService
) {

    @InteractionHandler(id = "change", idType = IDType.STARTS_WITH)
    fun change(event: ButtonInteractionEvent): RestAction<*> {
        val ids = event.getBeatmapIds()
        val embed = service.getBeatmapSetEmbed(ids[0], event.userLocale, ids[1])

        val buttons = event.message.buttons.map {
            if (it.id == null) return@map it
            if (it.label.contains("back", true)) {
                return@map it.withId("beatmapset:change:${embed.beatmap.beatmapSetId}-${embed.back}")
                    .withDisabled(!embed.hasBack())
            }
            if (it.label.contains("next", true)) {
                return@map it.withId("beatmapset:change:${embed.beatmap.beatmapSetId}-${embed.next}")
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
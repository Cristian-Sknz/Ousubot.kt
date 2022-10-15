package me.sknz.ousubot.app.interactions

import me.sknz.ousubot.domain.dto.ScoreRequest
import me.sknz.ousubot.domain.services.ScoreService
import me.sknz.ousubot.infrastructure.annotations.interaction.InteractionController
import me.sknz.ousubot.infrastructure.annotations.interaction.InteractionHandler
import me.sknz.ousubot.infrastructure.annotations.interaction.InteractionType
import me.sknz.ousubot.infrastructure.events.interactions.IDType
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.requests.RestAction

@InteractionController(type = InteractionType.BUTTON, id = "beatmapscore")
class BeatmapScoreButtonController(
    val service: ScoreService<*>
) {

    companion object {
        const val BEATMAP_SCORE_CHANGE = "beatmapscore:change:"
    }

    @InteractionHandler(id = "change", idType = IDType.STARTS_WITH)
    fun change(event: ButtonInteractionEvent): RestAction<*> {
        val ids = event.getScoreAndBeatmapIds()
        val embed = service.getBeatmapScoreEmbed(ScoreRequest(ids[0].toInt(), ids[1].toLong(), event.userLocale))

        val buttons = event.message.buttons.map {
            if (it.id == null) return@map it
            if (it.label.contains("back", true)) {
                return@map it.withId("$BEATMAP_SCORE_CHANGE${embed.payload}-${embed.back}")
                    .withDisabled(!embed.hasBack())
            }
            if (it.label.contains("next", true)) {
                return@map it.withId("$BEATMAP_SCORE_CHANGE${embed.payload}-${embed.next}")
                    .withDisabled(!embed.hasNext())
            }
            return@map it
        }
        return event.editMessageEmbeds(embed.toMessageEmbed())
            .setActionRow(buttons)
    }

    private fun ButtonInteractionEvent.getScoreAndBeatmapIds(): List<String> {
        return this.componentId.split(":")[2].split("-")
    }
}
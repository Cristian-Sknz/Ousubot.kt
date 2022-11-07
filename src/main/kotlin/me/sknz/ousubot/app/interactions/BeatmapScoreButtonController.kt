package me.sknz.ousubot.app.interactions

import me.sknz.ousubot.app.api.params.UserScoreParameter
import me.sknz.ousubot.domain.dto.DiscordPayload
import me.sknz.ousubot.domain.dto.ScoreRequest
import me.sknz.ousubot.domain.dto.UserScoreRequest
import me.sknz.ousubot.domain.services.ScoreService
import me.sknz.ousubot.infrastructure.annotations.interaction.InteractionController
import me.sknz.ousubot.infrastructure.annotations.interaction.InteractionHandler
import me.sknz.ousubot.infrastructure.annotations.interaction.InteractionType
import me.sknz.ousubot.infrastructure.events.interactions.IDType
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.requests.RestAction

@InteractionController(type = InteractionType.BUTTON, id = "scores")
class BeatmapScoreButtonController(
    val service: ScoreService<*>
) {

    companion object {
        const val BEATMAP_SCORE_CHANGE = "scores:beatmapchange:"
        const val USER_SCORE_CHANGE = "scores:userchange:"
    }

    @InteractionHandler(id = "beatmapchange", idType = IDType.STARTS_WITH)
    fun beatmapChange(event: ButtonInteractionEvent): RestAction<*> {
        val context = event.getBeatmapScorePayload()
        val value = when (event.button.label) {
            "Back" -> context.back
            "Next" -> context.next
            else -> throw RuntimeException("Opção invalida ao interagir com botão")
        }

        val embed = service.getBeatmapScoreEmbed(ScoreRequest(
            beatmap = context.payload!!,
            score = value,
            locale = event.userLocale)
        )
        val json = DiscordPayload(embed).json()

        val buttons = event.message.buttons.map {
            if (it.id == null) return@map it
            if (it.label.contains("back", true)) {
                return@map it.withId("$BEATMAP_SCORE_CHANGE${json}-back")
                    .withDisabled(!embed.hasBack())
            }
            if (it.label.contains("next", true)) {
                return@map it.withId("$BEATMAP_SCORE_CHANGE${json}-next")
                    .withDisabled(!embed.hasNext())
            }
            return@map it
        }
        return event.editMessageEmbeds(embed.toMessageEmbed())
            .setActionRow(buttons)
    }

    @InteractionHandler(id = "userchange", idType = IDType.STARTS_WITH)
    fun userChange(event: ButtonInteractionEvent): RestAction<*> {
        val context = event.getUserScorePayload()
        val value = when (event.button.label) {
            "Back" -> context.back
            "Next" -> context.next
            else -> throw RuntimeException("Opção invalida ao interagir com botão")
        }

        val embed = service.getUserScoreEmbed(UserScoreRequest(
            userId = context.payload!!.first,
            parameters = context.payload.second,
            score = value,
            locale = event.userLocale
        ))

        val json = DiscordPayload(embed).json()

        val buttons = event.message.buttons.map {
            if (it.id == null) return@map it
            if (it.label.contains("back", true)) {
                return@map it.withId("$USER_SCORE_CHANGE${json}-back")
                    .withDisabled(!embed.hasBack())
            }
            if (it.label.contains("next", true)) {
                return@map it.withId("$USER_SCORE_CHANGE${json}-next")
                    .withDisabled(!embed.hasNext())
            }
            return@map it
        }
        return event.editMessageEmbeds(embed.toMessageEmbed())
            .setActionRow(buttons)
    }

    private fun ButtonInteractionEvent.getUserScorePayload(): DiscordPayload<Pair<Int, UserScoreParameter>> {
        return DiscordPayload.from(this.componentId.substring(
            startIndex = USER_SCORE_CHANGE.length,
            endIndex = this.componentId.indexOfLast { it == '-' }
        ))
    }

    private fun ButtonInteractionEvent.getBeatmapScorePayload(): DiscordPayload<Int> {
        return DiscordPayload.from(this.componentId.substring(BEATMAP_SCORE_CHANGE.length,
            this.componentId.indexOfLast { it == '-' }))
    }
}
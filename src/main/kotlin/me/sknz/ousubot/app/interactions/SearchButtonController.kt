package me.sknz.ousubot.app.interactions

import me.sknz.ousubot.app.api.models.beatmaps.Beatmap
import me.sknz.ousubot.domain.dto.BeatmapSearchRequest
import me.sknz.ousubot.infrastructure.annotations.interaction.InteractionController
import me.sknz.ousubot.infrastructure.annotations.interaction.InteractionHandler
import me.sknz.ousubot.infrastructure.annotations.interaction.InteractionType
import me.sknz.ousubot.infrastructure.events.interactions.IDType
import me.sknz.ousubot.domain.services.SearchService
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.requests.RestAction
import net.dv8tion.jda.api.requests.restaction.interactions.MessageEditCallbackAction
import net.dv8tion.jda.api.utils.FileUpload
import java.net.URL
import java.util.*

@InteractionController(type = InteractionType.BUTTON, id = "searchmap")
class SearchButtonController(
    val service: SearchService<*>
) {

    companion object {
        const val SEARCH_CHANGE = "searchmap:change:"
    }

    @InteractionHandler(id = "change", idType = IDType.STARTS_WITH)
    fun change(event: ButtonInteractionEvent): RestAction<*> {
        val ids = event.getQueryAndBeatmapSetId()
        val request = BeatmapSearchRequest(String(Base64.getDecoder().decode(ids[0])), event.userLocale, ids[1].toInt())
        val embed = service.getSearchEmbed(request)

        val buttons = event.message.buttons.map {
            if (it.id == null) return@map it
            if (it.label.contains("back", true)) {
                return@map it.withId("$SEARCH_CHANGE${request.query}-${embed.back}")
                    .withDisabled(!embed.hasBack())
            }
            if (it.label.contains("next", true)) {
                return@map it.withId("$SEARCH_CHANGE${request.query}-${embed.next}")
                    .withDisabled(!embed.hasNext())
            }
            return@map it
        }
        return event.editMessageEmbeds(embed.toMessageEmbed())
            .setActionRow(buttons)
            .setBeatmapPreview(embed.payload)
    }

    fun MessageEditCallbackAction.setBeatmapPreview(beatmap: Beatmap): MessageEditCallbackAction {
        return this.setFiles(FileUpload.fromData(URL(beatmap.beatmapSet!!.previewUrl).openStream(), "${beatmap.id}.mp3"))
    }

    private fun ButtonInteractionEvent.getQueryAndBeatmapSetId(): List<String> {
        return this.componentId.split(":")[2].split("-")
    }
}
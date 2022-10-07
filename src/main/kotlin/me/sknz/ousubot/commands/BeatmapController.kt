package me.sknz.ousubot.commands

import me.sknz.ousubot.api.annotations.WorkInProgress
import me.sknz.ousubot.core.annotations.commands.*
import me.sknz.ousubot.dto.BeatmapSearchRequest
import me.sknz.ousubot.dto.BeatmapSetRequest
import me.sknz.ousubot.dto.DiscordBeatmapEmbed
import me.sknz.ousubot.interactions.BeatmapSetButtonController.Companion.BEATMAPSET_CHANGE
import me.sknz.ousubot.interactions.SearchButtonController.Companion.SEARCH_CHANGE
import me.sknz.ousubot.services.BeatmapService
import me.sknz.ousubot.services.SearchService
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.InteractionHook
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.requests.RestAction
import net.dv8tion.jda.api.requests.restaction.WebhookMessageCreateAction
import net.dv8tion.jda.api.utils.FileUpload
import net.dv8tion.jda.api.utils.messages.MessageCreateRequest
import java.net.URL

@SlashCommandController
@WorkInProgress
class BeatmapController(
    private var beatmapService: BeatmapService<*>,
    private var searchService: SearchService<*>,
) {

    @SlashCommand(name = "beatmap", description = "Get information from a beatmaps")
    @SlashCommandOptions([
        SlashCommandOption(
            name = "name",
            description = "Beatmap name or ID",
            required = true
    )])
    fun getBeatmap(interaction: SlashCommandInteraction,
                   @OptionParam("name") name: OptionMapping?): RestAction<*> {
        if (name?.asString?.toLongOrNull() != null) {
            val complete = interaction.deferReply(false).complete()
            val request = BeatmapSetRequest(null, name.asString.toInt(), interaction.userLocale)
            val embed = beatmapService.getBeatmapEmbed(request)

            return complete.sendBeatmapEmbed(BEATMAPSET_CHANGE, embed, null)
        }
        TODO("Implementar a pesquisa de mapa pelo comando /beatmap")
    }

    @SlashCommand(name = "beatmapset", description = "Get information from a set of beatmaps")
    @SlashCommandOptions([
        SlashCommandOption(
            name = "name",
            description = "BeatmapSet name or ID",
            required = true
    )])
    fun getBeatmapSet(interaction: SlashCommandInteraction,
                      @OptionParam("name") name: OptionMapping?): RestAction<*> {
        if (name?.asString?.toLongOrNull() != null) {
            val complete = interaction.deferReply().complete()
            val embed = beatmapService.getBeatmapEmbed(BeatmapSetRequest(name.asString.toInt(), null, interaction.userLocale))
            return complete.sendBeatmapEmbed(BEATMAPSET_CHANGE, embed, null)
        }
        TODO("Implementar a pesquisa de mapa pelo comando /beatmapset")
    }

    @SlashCommand(name = "search", description = "Search beatmap sets")
    @SlashCommandOptions([
        SlashCommandOption(
            name = "query",
            description = "Beatmapset name",
            required = true
    )])
    fun search(interaction: SlashCommandInteraction,
               @OptionParam("query") query: OptionMapping): RestAction<*> {
        val complete = interaction.deferReply().complete()
        val request = BeatmapSearchRequest(query.asString, interaction.userLocale)

        return complete.sendBeatmapEmbed(SEARCH_CHANGE, searchService.getSearchEmbed(request), request.query)
    }

    private fun <R: MessageCreateRequest<R>> MessageCreateRequest<R>.addButtons(prefix: String, embed: DiscordBeatmapEmbed, reference: String?): R {
        val back = Button.primary("$prefix${reference ?: embed.payload.beatmapSetId}-${embed.back}", "Back")
            .withDisabled(!embed.hasBack())
            .withEmoji(Emoji.fromUnicode("U+2B05"))

        val download = Button.link(embed.payload.url, "Download")

        val next = Button.primary("$prefix${reference ?: embed.payload.beatmapSetId}-${embed.next}", "Next")
            .withDisabled(!embed.hasNext())
            .withEmoji(Emoji.fromUnicode("U+27A1"))

        return this.addActionRow(back, download, next)
    }

    private fun InteractionHook.sendBeatmapEmbed(prefix: String, embed: DiscordBeatmapEmbed, reference: String?): WebhookMessageCreateAction<Message> {
        return this.sendMessageEmbeds(embed.toMessageEmbed())
            .addButtons(prefix, embed, reference)
            .addFiles(FileUpload.fromData(URL(embed.payload.beatmapSet!!.previewUrl).openStream(), "${embed.payload.id}.mp3"))
    }
}
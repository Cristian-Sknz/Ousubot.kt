package me.sknz.ousubot.commands

import me.sknz.ousubot.api.annotations.WorkInProgress
import me.sknz.ousubot.api.models.beatmaps.Beatmap
import me.sknz.ousubot.core.annotations.commands.*
import me.sknz.ousubot.dto.BeatmapRequest
import me.sknz.ousubot.services.BeatmapService
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.requests.RestAction
import net.dv8tion.jda.api.utils.FileUpload
import net.dv8tion.jda.api.utils.messages.MessageCreateRequest
import java.net.URL

@SlashCommandController
@WorkInProgress
class BeatmapController(
    private var service: BeatmapService
) {

    @SlashCommand(name = "beatmap", description = "Get a beatmap")
    @SlashCommandOptions([
        SlashCommandOption(
            name = "name",
            description = "Beatmap name or beatmap id",
            required = true
    )])
    fun getBeatmap(interaction: SlashCommandInteraction,
                   @OptionParam("name") name: OptionMapping?
    ): RestAction<*> {
        if (name?.asString?.toLongOrNull() != null) {
            val complete = interaction.deferReply(false).complete()
            val embed = service.getBeatmapEmbed(BeatmapRequest(name.asInt, interaction.userLocale))
            val beatmap = embed.beatmap!!

            return complete.sendMessageEmbeds(embed.toMessageEmbed())
                .addActionRow(Button.link(beatmap.url, "Download"))
                .addBeatmapPreview(embed.beatmap!!)
        }

        return interaction.notImplemented()
    }

    @SlashCommand(name = "beatmapset", description = "Get a beatmapset")
    @SlashCommandOptions([
        SlashCommandOption(
            name = "name",
            description = "Beatmapset name or beatmapset id",
            required = true
    )])
    fun getBeatmapSet(interaction: SlashCommandInteraction,
                      @OptionParam("name") name: OptionMapping?): RestAction<*> {
        if (name?.asString?.toLongOrNull() != null) {
            val complete = interaction.deferReply().complete()
            val embed = service.getBeatmapSetEmbed(name.asString.toInt(), interaction.userLocale)

            val back = Button.primary("beatmapset:change:${embed.beatmap.beatmapSetId}-${embed.back}", "Back")
                .withDisabled(!embed.hasBack())
                .withEmoji(Emoji.fromUnicode("U+2B05"))

            val download = Button.link(embed.beatmap.url, "Download")

            val next = Button.primary("beatmapset:change:${embed.beatmap.beatmapSetId}-${embed.next}", "Next")
                .withDisabled(!embed.hasNext())
                .withEmoji(Emoji.fromUnicode("U+27A1"))

            return complete.sendMessageEmbeds(embed.toMessageEmbed())
                .addActionRow(back, download, next)
                .addBeatmapPreview(embed.beatmap)
        }
        return interaction.notImplemented()
    }

    fun <R : MessageCreateRequest<R>> MessageCreateRequest<R>.addBeatmapPreview(beatmap: Beatmap): R {
        return this.addFiles(FileUpload.fromData(URL(beatmap.beatmapSet!!.previewUrl).openStream(), "${beatmap.id}.mp3"))
    }

    fun SlashCommandInteraction.notImplemented() =
        this.reply("Está função ainda não está implementada").setEphemeral(true)

}
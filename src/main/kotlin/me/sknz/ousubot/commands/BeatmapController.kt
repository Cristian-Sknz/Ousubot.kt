package me.sknz.ousubot.commands

import me.sknz.ousubot.api.annotations.WorkInProgress
import me.sknz.ousubot.core.annotations.commands.*
import me.sknz.ousubot.dto.BeatmapRequest
import me.sknz.ousubot.services.BeatmapService
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction
import net.dv8tion.jda.api.requests.RestAction
import net.dv8tion.jda.api.utils.FileUpload
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
    fun getBeatmap(
        interaction: SlashCommandInteraction,
        @OptionParam("name") name: OptionMapping?
    ): RestAction<*> {
        if (name?.asString?.toLongOrNull() != null) {
            val complete = interaction.deferReply(false).complete()
            val embed = service.getBeatmapEmbed(BeatmapRequest(name.asInt, interaction.userLocale))
            val beatmap = embed.beatmap!!

            return complete.sendMessageEmbeds(embed.toMessageEmbed())
                .addFiles(
                    FileUpload.fromData(
                        URL(beatmap.beatmapSet!!.previewUrl).openStream(), "${beatmap.beatmapSetId}.mp3"
                    )
                )
        }

        return interaction.notImplemented()
    }

    fun SlashCommandInteraction.notImplemented() =
        this.reply("Está função ainda não está implementada").setEphemeral(true)
}
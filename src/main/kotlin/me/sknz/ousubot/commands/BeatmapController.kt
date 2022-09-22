package me.sknz.ousubot.commands

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import me.sknz.ousubot.api.OsuClientAPI
import me.sknz.ousubot.api.annotations.WorkInProgress
import me.sknz.ousubot.core.annotations.commands.*
import me.sknz.ousubot.core.xml.DiscordEmbed
import me.sknz.ousubot.utils.ColorThief
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction
import net.dv8tion.jda.api.requests.RestAction
import net.dv8tion.jda.api.utils.FileUpload
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.SpringTemplateEngine
import java.net.URL

@SlashCommandController
@WorkInProgress
class BeatmapController(
    private var client: OsuClientAPI
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
        @OptionParam("name") name: OptionMapping?,
        @FromBean engine: SpringTemplateEngine
    ): RestAction<*> {
        if (name?.asString?.toLongOrNull() != null) {
            val ctx = Context()
            val beatmap = client.getBeatmap(name.asString.toInt())
            ctx.setVariable("beatmap", beatmap)
            ctx.setVariable("color", ColorThief.getPredominatColor(beatmap.beatmapSet!!.covers.card, true).rgb)
            val xml = engine.process("BeatmapInfo", ctx)

            val complete = interaction.deferReply(false).complete()
            val mapper = XmlMapper()
                .findAndRegisterModules()

            return complete.sendMessageEmbeds(mapper.readValue(xml, DiscordEmbed::class.java).toMessageEmbed())
                .addFiles(
                    FileUpload.fromData(
                        URL(beatmap.beatmapSet.previewUrl).openStream(),
                        "${beatmap.beatmapSetId}.mp3"
                    )
                )
        }

        return interaction.notImplemented()
    }

    fun SlashCommandInteraction.notImplemented() =
        this.reply("Está função ainda não está implementada").setEphemeral(true)
}
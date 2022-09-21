package me.sknz.ousubot.commands

import me.sknz.ousubot.api.OsuClientAPI
import me.sknz.ousubot.api.annotations.WorkInProgress
import me.sknz.ousubot.core.annotations.commands.*
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction
import net.dv8tion.jda.api.requests.RestAction

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
        )
    ])
    fun getBeatmap(interaction: SlashCommandInteraction): RestAction<*> {
        val name = interaction.getOption("name")?.asString
        if (name?.toLongOrNull() != null) {
            println(client.getBeatmap(name.toInt()))
            return interaction.notImplemented()
        }

        return interaction.notImplemented()
    }

    fun SlashCommandInteraction.notImplemented() =
        this.reply("Está função ainda não está implementada").setEphemeral(true)
}
package me.sknz.ousubot.commands

import me.sknz.ousubot.api.OsuClientAPI
import me.sknz.ousubot.api.annotations.WorkInProgress
import me.sknz.ousubot.core.annotations.commands.SlashCommand
import me.sknz.ousubot.core.annotations.commands.SlashCommandController
import me.sknz.ousubot.core.annotations.commands.SlashCommandOption
import me.sknz.ousubot.core.annotations.commands.SlashCommandOptions
import me.sknz.ousubot.core.context.SlashCommandContext
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.requests.RestAction

@SlashCommandController
@WorkInProgress
class BeatmapController(
    private var context: SlashCommandContext,
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
    fun getBeatmap(): RestAction<*> {
        val event = context.event
        val name = event.getOption("name")?.asString

        if (name?.toLongOrNull() != null) {
            println(client.getBeatmap(name.toInt()))
            return event.notImplemented()
        }

        return event.notImplemented()
    }

    fun SlashCommandInteractionEvent.notImplemented() =
        this.reply("Está função ainda não está implementada").setEphemeral(true)
}
package me.sknz.ousubot.commands

import me.sknz.ousubot.api.OsuClientAPI
import me.sknz.ousubot.core.annotations.SlashCommand
import me.sknz.ousubot.core.annotations.SlashCommandController
import me.sknz.ousubot.core.annotations.SlashCommandOption
import me.sknz.ousubot.core.annotations.SlashCommandOptions
import me.sknz.ousubot.core.context.SlashCommandContext
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.requests.RestAction

@SlashCommandController
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
            return event.notImplemented()
        }

        return event.notImplemented()
    }

    fun SlashCommandInteractionEvent.notImplemented() =
        this.reply("Está função ainda não está implementada").setEphemeral(true)
}
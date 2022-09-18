package me.sknz.ousubot.core.context

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent

open class SlashCommandContext {
    lateinit var event: SlashCommandInteractionEvent

}
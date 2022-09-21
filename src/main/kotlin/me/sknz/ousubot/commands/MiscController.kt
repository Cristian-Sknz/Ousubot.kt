package me.sknz.ousubot.commands

import me.sknz.ousubot.api.annotations.WorkInProgress
import me.sknz.ousubot.core.annotations.commands.SlashCommand
import me.sknz.ousubot.core.annotations.commands.SlashCommandController
import net.dv8tion.jda.api.interactions.commands.CommandInteraction
import net.dv8tion.jda.api.interactions.components.Modal
import net.dv8tion.jda.api.interactions.components.text.TextInput
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import net.dv8tion.jda.api.requests.RestAction
import org.springframework.beans.factory.annotation.Value

@WorkInProgress
@SlashCommandController
class MiscController(
    @Value("\${application.discord.ownerId}") private val ownerId: String,
) {

    @SlashCommand(name="token", "Bot Internal Command")
    fun setOsuToken(interaction: CommandInteraction): RestAction<*> {
        if (ownerId != interaction.user.id) {
            return interaction.reply("Você não tem permissão para acessar comandos internos")
                .setEphemeral(true)
        }

        val input = TextInput.create("token", "Token", TextInputStyle.PARAGRAPH)
            .setPlaceholder("Token Code")
            .build()

        val modal = Modal.create("osu-token", "Provides a Osu Token")
        modal.addActionRow(input)

        return interaction.replyModal(modal.build())
    }

}
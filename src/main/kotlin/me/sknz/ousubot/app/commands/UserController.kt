package me.sknz.ousubot.app.commands

import me.sknz.ousubot.infrastructure.annotations.commands.*
import me.sknz.ousubot.domain.dto.UserRequest
import me.sknz.ousubot.domain.services.UserService
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction
import net.dv8tion.jda.api.requests.RestAction
import net.dv8tion.jda.api.utils.FileUpload
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

@SlashCommandController
class UserController(
    private var userService: UserService<*>
) {

    @SlashCommand(name = "user", description = "Get basic information from an Osu user!")
    @SlashCommandOptions([
        SlashCommandOption(
            name = "name",
            description = "User name",
            required = true
    )])
    fun getUser(interaction: SlashCommandInteraction,
                @OptionParam("name") name: OptionMapping): RestAction<*> {
        val complete = interaction.deferReply().complete()
        val embed = userService.getUserEmbed(UserRequest(name.asString, interaction.userLocale))
        val stream = ByteArrayOutputStream()
        ImageIO.write(embed.payload.image, "png", stream)

        return complete.sendMessageEmbeds(embed.toMessageEmbed()).setFiles(
            FileUpload.fromData(stream.toByteArray(), "user_image.png")
        )
    }
}
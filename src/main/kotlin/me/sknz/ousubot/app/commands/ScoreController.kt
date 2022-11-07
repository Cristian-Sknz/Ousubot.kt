package me.sknz.ousubot.app.commands

import me.sknz.ousubot.app.api.params.UserScoreParameter
import me.sknz.ousubot.app.interactions.BeatmapScoreButtonController.Companion.BEATMAP_SCORE_CHANGE
import me.sknz.ousubot.domain.dto.*
import me.sknz.ousubot.domain.services.ScoreService
import me.sknz.ousubot.infrastructure.annotations.commands.*
import me.sknz.ousubot.infrastructure.annotations.commands.SlashCommand.Option
import me.sknz.ousubot.infrastructure.annotations.commands.SlashCommand.Options
import me.sknz.ousubot.infrastructure.tools.BeatmapDetector
import me.sknz.ousubot.infrastructure.tools.DiscordI18nBundle.reply
import net.dv8tion.jda.api.entities.emoji.Emoji
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent
import net.dv8tion.jda.api.interactions.commands.CommandInteraction
import net.dv8tion.jda.api.interactions.commands.OptionMapping
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.components.buttons.Button
import net.dv8tion.jda.api.requests.RestAction
import net.dv8tion.jda.api.utils.messages.MessageCreateRequest

@SlashCommandController(name="scores")
class ScoreController(
    private val scoreService: ScoreService<*>
) {

    @SlashCommand(name="beatmap", description="Get a Beatmap Top Scores")
    @Option(OptionType.INTEGER, name = "id", description = "Beatmap Id", required = true)
    @MessageInteraction("View Beatmap Top Scores")
    fun beatmapScore(interaction: CommandInteraction, @OptionParam("id") id: OptionMapping?): RestAction<*> {
        if (interaction is MessageContextInteractionEvent) {
            val detection = BeatmapDetector.detect(interaction.target)
            if (detection.beatmap == null) {
                return interaction.reply("osu.exceptions.notfound.view_beatmap_top_scores", interaction.userLocale)
            }

            val hook = interaction.deferReply().complete()

            val embed = scoreService.getBeatmapScoreEmbed(ScoreRequest(detection.beatmap, null, interaction.userLocale))
            return hook.sendMessageEmbeds(embed.toMessageEmbed()).addButtons(BEATMAP_SCORE_CHANGE, embed)
        }
        val hook = interaction.deferReply().complete()
        val embed = scoreService.getBeatmapScoreEmbed(ScoreRequest(id!!.asInt, null, interaction.userLocale))
        return hook.sendMessageEmbeds(embed.toMessageEmbed()).addButtons(BEATMAP_SCORE_CHANGE, embed)
    }

    @SlashCommand(name="user", description="Get a User Scores")
    @Options([
        Option(OptionType.INTEGER, name = "id", description = "User Id", required = true),
        Option(OptionType.STRING, name = "mode", description = "Game mode",
            choices = [
                Option.Choice("Fruits",  "osu!catch"),
                Option.Choice("Mania",  "osu!mania"),
                Option.Choice("Osu",  "osu!standard"),
                Option.Choice("Taiko",  "osu!taiko"),
            ]
        ),
        Option(OptionType.STRING, name = "type", description = "Score Type",
            choices = [
                Option.Choice("Best Scores", "best"),
                Option.Choice("Recent Scores", "recent"),
                Option.Choice("Firsts", "firsts")
            ]
        ),
    ])
    fun userScore(interaction: CommandInteraction, @OptionParam("id") id: OptionMapping): RestAction<*> {
        val request = UserScoreRequest(
            userId = id.asInt,
            parameters = UserScoreParameter.from(interaction.options),
            null,
            locale = interaction.userLocale
        )
        val hook = interaction.deferReply(false).complete()
        val embed = scoreService.getUserScoreEmbed(request)

        return hook.sendMessageEmbeds(embed.toMessageEmbed())
    }

    private fun <R: MessageCreateRequest<R>> MessageCreateRequest<R>.addButtons(prefix: String, embed: AbstractDiscordEmbed<*>): R {
        val json = DiscordPayload(embed).json()
        val back = Button.primary("$prefix${json}-back", "Back")
            .withDisabled(!embed.hasBack())
            .withEmoji(Emoji.fromUnicode("U+2B05"))

        val next = Button.primary("$prefix${json}-next", "Next")
            .withDisabled(!embed.hasNext())
            .withEmoji(Emoji.fromUnicode("U+27A1"))

        return this.addActionRow(back, next)
    }
}
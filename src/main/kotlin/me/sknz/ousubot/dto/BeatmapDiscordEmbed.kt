package me.sknz.ousubot.dto

import me.sknz.ousubot.api.models.beatmaps.Beatmap
import me.sknz.ousubot.core.xml.DiscordEmbed
import java.io.Serializable

class BeatmapDiscordEmbed : DiscordEmbed(), Serializable {
    var beatmap: Beatmap? = null
}
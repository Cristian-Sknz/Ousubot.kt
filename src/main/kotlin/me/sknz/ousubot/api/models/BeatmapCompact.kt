package me.sknz.ousubot.api.models

import com.fasterxml.jackson.annotation.JsonProperty
import me.sknz.ousubot.api.models.enums.GameMode

open class BeatmapCompact {
    val id: Int = 0
    val mode: GameMode = GameMode.Osu
    val status: String = ""
    val version: String = ""

    @JsonProperty("beatmapset_id")
    val beatmapSetId: Int = 0

    @JsonProperty("difficultRating")
    val difficultRatting: Float = 0F

    @JsonProperty("total_length")
    val totalLength: Int = 0

    @JsonProperty("user_id")
    val userId: Int = 0
}

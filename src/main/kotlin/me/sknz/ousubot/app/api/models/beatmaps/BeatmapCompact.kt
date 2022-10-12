package me.sknz.ousubot.app.api.models.beatmaps

import com.fasterxml.jackson.annotation.JsonProperty
import me.sknz.ousubot.app.api.models.enums.GameMode
import java.io.Serializable

open class BeatmapCompact: Serializable {
    val id: Int = 0
    val mode: GameMode = GameMode.Osu
    val status: String = ""
    val version: String = ""

    @JsonProperty("beatmapset_id")
    val beatmapSetId: Int = 0

    @JsonProperty("difficulty_rating")
    val difficultyRating: Float = 0F

    @JsonProperty("total_length")
    val totalLength: Int = 0

    @JsonProperty("user_id")
    val userId: Int = 0
}

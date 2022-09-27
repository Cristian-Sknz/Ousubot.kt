package me.sknz.ousubot.api.models.beatmaps

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

class BeatmapSearch : Serializable {

    @JsonProperty("beatmapsets")
    val beatmapSets: List<BeatmapSet> = emptyList()
    val error: String? = null
    val total = 0

    @JsonProperty("recommended_difficulty")
    val recommendedDifficulty = 0F
}
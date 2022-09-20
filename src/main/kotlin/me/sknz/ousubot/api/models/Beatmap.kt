package me.sknz.ousubot.api.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.OffsetDateTime

class Beatmap : BeatmapCompact() {
    val accuracy: Float = 0F
    val ar: Float = 0F
    val bpm: Float? = null
    val convert: Boolean = false
    val cs: Float = 0F
    val drain: Float = 0F
    val ranked: Int = 0
    val url: String = ""

    @JsonProperty("is_scoreable")
    val scoreable: Boolean = false

    @JsonProperty("last_updated")
    val lastUpdated: OffsetDateTime = OffsetDateTime.now()

    @JsonProperty("mode_int")
    val modeInt: Int = 0

    @JsonProperty("passcount")
    val passCount: Int = 0

    @JsonProperty("playcount")
    val playCount: Int = 0

    @JsonProperty("count_circles")
    val countCircles: Int = 0
    @JsonProperty("count_sliders")
    val countSliders: Int = 0
    @JsonProperty("count_spinners")
    val countSpinners: Int = 0
    @JsonProperty("deleted_at")
    val deletedAt: OffsetDateTime? = null
    @JsonProperty("hit_length")
    val hitLength: Int = 0
}
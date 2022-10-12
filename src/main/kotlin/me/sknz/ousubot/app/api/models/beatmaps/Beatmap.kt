package me.sknz.ousubot.app.api.models.beatmaps

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.text.DecimalFormat
import java.time.OffsetDateTime
import kotlin.math.roundToInt

open class Beatmap : BeatmapCompact(), Serializable {
    val accuracy: Float = 0F
    val ar: Float = 0F
    val bpm: Float? = null
    val convert: Boolean = false
    val cs: Float = 0F
    val drain: Float = 0F
    val ranked: Int = 0
    val url: String = ""

    @JsonProperty("beatmapset")
    var beatmapSet: BeatmapSet? = null

    @JsonProperty("is_scoreable")
    val scoreable: Boolean = false

    @JsonProperty("last_updated")
    val lastUpdated: OffsetDateTime = OffsetDateTime.now()

    @JsonProperty("mode_int")
    val modeInt: Int = 0

    @JsonProperty("passcount")
    val passCount: Int = 1

    @JsonProperty("playcount")
    val playCount: Int = 1

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

    val checksum: String? = null
    @JsonProperty("failtimes")
    val failTimes: FailTimes? = null
    @JsonProperty("max_combo")
    val maxCombo: Int? = null
    class FailTimes: Serializable {
        val fail: Array<Int> = emptyArray()
        val exit: Array<Int> = emptyArray()
    }

    fun getStars(): String {
        val stars = "✩✩✩✩✩✩✩".toCharArray()
        for (star in 1..this.difficultyRating.roundToInt()) {
            if (star > 7) break
            stars[star - 1] = '★'
        }

        return stars.joinToString("")
    }

    fun getSuccessRate(): String {
        return "${DecimalFormat("#.##").format((passCount.toDouble() * 100) / playCount.toDouble())}%"
    }
}
package me.sknz.ousubot.app.api.models.scores

import com.fasterxml.jackson.annotation.JsonProperty
import me.sknz.ousubot.app.api.models.beatmaps.Beatmap
import me.sknz.ousubot.app.api.models.beatmaps.BeatmapSet
import me.sknz.ousubot.app.api.models.users.UserCompact
import java.io.Serializable
import java.text.DecimalFormat
import java.time.OffsetDateTime

class BeatmapScore : Serializable {

    val id:  Long  = 0
    @JsonProperty("best_id")
    val bestId: Long = 0
    @JsonProperty("user_id")
    val userId: Int = 0
    val accuracy: Double = Double.NaN
    val mods: Array<String> = emptyArray()
    val score: Long = 0
    val type: String = ""
    @JsonProperty("max_combo")
    val maxCombo: Int = 0
    val perfect: Boolean = false
    val statistics: ScoreStatistic = ScoreStatistic()

    class ScoreStatistic: Serializable {
        @JsonProperty("count_50")
        val count50: Int = 0
        @JsonProperty("count_100")
        val count100: Int = 0
        @JsonProperty("count_300")
        val count300: Int = 0
        @JsonProperty("count_geki")
        val countGeki: Int = 0
        @JsonProperty("count_katu")
        val countKatu: Int = 0
        @JsonProperty("count_miss")
        val countMiss: Int = 0
    }

    val passed: Boolean = false
    val pp: Double = Double.NaN
    val rank: String =  ""
    @JsonProperty("created_at")
    val createdAt = OffsetDateTime.now()
    val mode: String = ""
    @JsonProperty("mode_int")
    val modeInt: Int = 0
    val replay: Boolean = false

    var beatmap: Beatmap? = null
    @JsonProperty("beatmapset")
    var beatmapSet: BeatmapSet? = null

    @JsonProperty("rank_country")
    val rankCountry: Int? = null
    @JsonProperty("rank_global")
    val rankGlobal: Int? = null
    val weight: ScoreWeight? = null
    val user: UserCompact? = null
    val match: Any? = null

    fun getAccuracy(): String {
        return DecimalFormat("##.##").format(accuracy * 100)
    }

    class ScoreWeight: Serializable {
        val pp: Double = Double.NaN
        val percentage: Double = Double.NaN
    }
}
package me.sknz.ousubot.api.models.beatmaps

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.time.OffsetDateTime

class BeatmapSet: BeatmapSetCompact(), Serializable, Cloneable {

    val spotlight: Boolean = false
    @JsonProperty("track_id")
    val trackId: Int = 0

    @JsonProperty("discussion_enabled")
    val discussionEnabled: Boolean = false

    val storyboard: Boolean = false
    @JsonProperty("submitted_date")
    val submittedDate: OffsetDateTime? = null
    val source: String = ""
    @JsonProperty("ranked_date")
    val rankedDate: OffsetDateTime? = null
    val bpm: Float = 0F
    @JsonProperty("can_be_hyped")
    val canBeHyped: Boolean = false
    @JsonProperty("discussion_locked")
    val discussionLocked: Boolean = false
    val ranked = 1
    val tags: String? = null
    val availability: BeatmapSetAvailability? = null
    @JsonProperty("nominations_summary")
    val nominationsSummary: NominationsSummary? = null
    @JsonProperty("is_scoreable")
    val scoreable = false
    val hype: Any? = null
    @JsonProperty("last_updated")
    val lastUpdated: OffsetDateTime = OffsetDateTime.now()
    @JsonProperty("legacy_thread_url")
    val legacyThreadUrl: String? = null

    class BeatmapSetAvailability: Serializable {
        @JsonProperty("download_disabled")
        val downloadDisabled: Boolean = false
        @JsonProperty("more_information")
        val moreInformation: Any? = null
    }

    class NominationsSummary: Serializable {
        val current: Int = 0
        val required: Int = 0
    }

    fun getBeatmapSetUrl(): String {
        return "https://osu.ppy.sh/beatmapsets/$id"
    }

    public override fun clone(): Any {
        return super.clone()
    }

    fun cloneWithoutBeatmaps(): BeatmapSet {
        return (super.clone() as BeatmapSet).let {
            it.beatmaps = null
            return@let it
        }
    }
}
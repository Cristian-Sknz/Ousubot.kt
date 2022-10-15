package me.sknz.ousubot.app.api.models.users

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.time.LocalDate
import java.time.OffsetDateTime

open class UserCompact : Serializable {

    val id: Int = 0
    val username: String = ""

    @JsonProperty("profile_colour")
    val profileColour: String? = null

    @JsonProperty("avatar_url")
    val avatarUrl: String = ""

    @JsonProperty("country_code")
    val countryCode: String = ""

    @JsonProperty("is_active")
    val active: Boolean = false

    @JsonProperty("is_bot")
    val bot: Boolean = false

    @JsonProperty("is_deleted")
    val deleted: Boolean = false

    @JsonProperty("is_online")
    val online: Boolean = false

    @JsonProperty("is_supporter")
    val supporter: Boolean = false

    @JsonProperty("active_tournament_banner")
    val activeTournamentBanner: ProfileBanner? = null
    val accountHistory: Array<AccountHistory> = emptyArray()
    val badges: Array<Badge> = emptyArray()

    val country = PlayerCountry()
    val cover = Cover()

    @JsonProperty("is_restricted")
    val restricted: Boolean? = null

    val groups: Array<PlayerGroup> = emptyArray()

    @JsonProperty("beatmap_playcounts_count")
    val beatmapPlayCount: Int? = null

    @JsonProperty("favourite_beatmapset_count")
    val favouriteBeatmapsetCount: Int? = null

    @JsonProperty("follower_count")
    val followerCount: Int? = null

    @JsonProperty("graveyard_beatmapset_count")
    val graveyardBeatmapSetCount: Int? = null

    @JsonProperty("loved_beatmapset_count")
    val lovedBeatmapsetCount: Int? = null

    @JsonProperty("monthly_playcounts")
    val monthlyPlayCounts: Array<GenericCount> = emptyArray()

    @JsonProperty("pending_beatmapset_count")
    val pendingBeatmapsetCount: Int? = null

    @JsonProperty("previous_usernames")
    val previousUsernames: Array<String> = emptyArray()

    @JsonProperty("ranked_beatmapset_count")
    val rankedBeatmapSetCount: Int? = null

    @JsonProperty("replays_watched_counts")
    val replaysWatchedCounts: Array<GenericCount>? = null

    @JsonProperty("scores_best_count")
    val scoresBestCount: Int? = null

    @JsonProperty("scores_first_count")
    val scoresFirstCount: Int? = null

    @JsonProperty("scores_pinned_count")
    val scoresPinnedCount: Int? = null

    @JsonProperty("scores_recent_count")
    val scoresRecentCount: Int? = null
    val statistics: PlayerStatistics? = null

    @JsonProperty("statistics_rulesets")
    val statisticsRulesets: StatisticsRuleSets? = null

    @JsonProperty("support_level")
    val supportLevel: Int = 0

    @JsonProperty("user_achievements")
    val userAchievements: Array<UserAchievement>? = null;

    class ProfileBanner : Serializable {
        val id: Int = 0

        @JsonProperty("tournament_id")
        val tournamentId: Int = 0
        val image: String = ""
    }

    class Badge : Serializable {
        @JsonProperty("awarded_at")
        val awardedAt: OffsetDateTime = OffsetDateTime.now()
        val description: String = ""

        @JsonProperty("image_url")
        val imageUrl: String = ""
        val url: String = ""
    }

    class AccountHistory : Serializable {
        val description: String? = null
        val id: Int = 0
        val length: Int = 0
        val permanent: Boolean = false
        val timestamp: OffsetDateTime = OffsetDateTime.now()
        val type: String = ""
    }

    class GenericCount : Serializable {
        @JsonProperty("start_date")
        val startDate: LocalDate = LocalDate.now()
        val count: Int = 0
    }

    class PlayerStatistics : Serializable {

        val level = Level()

        @JsonProperty("global_rank")
        val globalRank: Int? = null
        val pp: Int = 0

        @JsonProperty("ranked_score")
        val rankedScore: Long = 0

        @JsonProperty("hit_accuracy")
        val hitAccuracy: Float = 0F

        @JsonProperty("play_count")
        val playCount: Int = 0

        @JsonProperty("play_time")
        val playTime: Int = 0

        @JsonProperty("total_score")
        val totalScore: Long = 0

        @JsonProperty("total_hits")
        val totalHits: Int = 0

        @JsonProperty("maximum_combo")
        val maximumCombo: Int = 0

        @JsonProperty("replays_watched_by_others")
        val replaysWatchedByOthers: Int = 0

        @JsonProperty("is_ranked")
        val ranked: Boolean = false

        @JsonProperty("grade_counts")
        val grade = Grade()

        @JsonProperty("country_rank")
        val countryRank: Int? = null

        class Grade : Serializable {
            val ss: Int = 0
            val ssh: Int = 0
            val s: Int = 0
            val sh: Int = 0
            val a: Int = 0

            fun values(): IntArray = intArrayOf(ss, ssh, s, sh, a)
        }

        class Level : Serializable {
            val current: Int = 0
            val progress: Int = 0
        }
    }

    class StatisticsRuleSets : Serializable {
        val osu: PlayerStatistics? = null
        val taiko: PlayerStatistics? = null
        val fruits: PlayerStatistics? = null
        val mania: PlayerStatistics? = null
    }

    class Cover : Serializable {
        @JsonProperty("custom_url")
        val customUrl: String? = null
        val url: String = ""
        val id: String? = null
    }

    class PlayerCountry : Serializable {
        val code: String = ""
        val name: String = ""
    }

    class PlayerGroup : Serializable {
        val colour: String? = null

        @JsonProperty("has_listing")
        val hasListing: Boolean = false

        @JsonProperty("has_playmodes")
        val hasPlayModes: Boolean = false
        val id: Int = 0
        val identifier: String = ""

        @JsonProperty("is_probationary")
        val probationary: Boolean = false
        val name: String = ""

        @JsonProperty("short_name")
        val shortName: String = ""
    }

    class UserAchievement : Serializable {
        @JsonProperty("achieved_at")
        val achievedAt: OffsetDateTime = OffsetDateTime.now()

        @JsonProperty("achievement_id")
        val achievementId: Int = 0
    }

    fun getUrl(): String {
        return "https://osu.ppy.sh/users/$id"
    }
}
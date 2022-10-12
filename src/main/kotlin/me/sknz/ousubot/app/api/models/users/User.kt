package me.sknz.ousubot.app.api.models.users

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.time.LocalDate
import java.time.OffsetDateTime

class User : UserCompact(), Serializable {

    @JsonProperty("default_group")
    val defaultGroup: String = ""

    @JsonProperty("last_visit")
    val lastVisit: OffsetDateTime = OffsetDateTime.now()

    @JsonProperty("pm_friends_only")
    val pmFriendsOnly: Boolean = false

    @JsonProperty("cover_url")
    val coverUrl: String = ""
    val discord: String? = null

    @JsonProperty("has_supported")
    val supported: Boolean = false
    val interests: String? = null

    @JsonProperty("join_date")
    val joinDate: OffsetDateTime = OffsetDateTime.now()

    val kudosu: Kudosu? = null
    val location: String? = null

    @JsonProperty("max_blocks")
    val maxBlocks: Int = 0

    @JsonProperty("max_friends")
    val maxFriends: Int = 0
    val occupation: String? = null

    @JsonProperty("playmode")
    val playMode: String? = null

    @JsonProperty("playstyle")
    val playStyle: Array<String>? = null

    @JsonProperty("post_count")
    val postCount: Int = 0

    @JsonProperty("profile_order")
    val profileOrder: Array<String> = emptyArray()

    val title: String? = null

    @JsonProperty("title_url")
    val titleUrl: String? = null
    val twitter: String? = null
    val website: String? = null
    val country = PlayerCountry()
    val cover = Cover()

    @JsonProperty("is_restricted")
    val restricted: Boolean? = null

    @JsonProperty("active_tournament_banner")
    val activeTournamentBanner: ProfileBanner? = null
    val accountHistory: Array<AccountHistory> = emptyArray()
    val badges: Array<Badge> = emptyArray()
    val groups: Array<PlayerGroup> = emptyArray()

    @JsonProperty("beatmap_playcounts_count")
    val beatmapPlayCount: Int? = null

    @JsonProperty("comments_count")
    val commentsCount: Int? = null

    @JsonProperty("favourite_beatmapset_count")
    val favouriteBeatmapsetCount: Int? = null

    @JsonProperty("follower_count")
    val followerCount: Int? = null

    @JsonProperty("graveyard_beatmapset_count")
    val graveyardBeatmapSetCount: Int? = null

    @JsonProperty("guest_beatmapset_count")
    val guestBeatmapsSetCount: Int? = null

    @JsonProperty("loved_beatmapset_count")
    val lovedBeatmapsetCount: Int? = null

    @JsonProperty("mapping_follower_count")
    val mappingFollowerCount: Int? = null

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

    @JsonProperty("rank_history")
    val rankHistory: RankHistory? = null

    @JsonProperty("ranked_and_approved_beatmapset_count")
    val rankedAndApprovedBeatmapSetCount: Int? = null

    @JsonProperty("unranked_beatmapset_count")
    val unrankedBeatmapSetCount: Int? = null

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

    class Kudosu : Serializable {
        val total: Int = 0
        val available: Int = 0
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

    class UserAchievement : Serializable {
        @JsonProperty("achieved_at")
        val achievedAt: OffsetDateTime = OffsetDateTime.now()

        @JsonProperty("achievement_id")
        val achievementId: Int = 0
    }

    class RankHistory : Serializable {
        val mode: String = ""
        val data: IntArray = intArrayOf()
    }
}
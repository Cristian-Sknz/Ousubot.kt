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

    @JsonProperty("comments_count")
    val commentsCount: Int? = null

    @JsonProperty("guest_beatmapset_count")
    val guestBeatmapsSetCount: Int? = null

    @JsonProperty("mapping_follower_count")
    val mappingFollowerCount: Int? = null

    @JsonProperty("rank_history")
    val rankHistory: RankHistory? = null

    @JsonProperty("ranked_and_approved_beatmapset_count")
    val rankedAndApprovedBeatmapSetCount: Int? = null

    @JsonProperty("unranked_beatmapset_count")
    val unrankedBeatmapSetCount: Int? = null

    class Kudosu : Serializable {
        val total: Int = 0
        val available: Int = 0
    }

    class RankHistory : Serializable {
        val mode: String = ""
        val data: IntArray = intArrayOf()
    }
}
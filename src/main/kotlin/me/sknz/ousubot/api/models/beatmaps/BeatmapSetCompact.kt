package me.sknz.ousubot.api.models.beatmaps

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

open class BeatmapSetCompact: Serializable {

    val artist: String = ""
    @JsonProperty("artist_unicode")
    val artistUnicode: String? = null
    val covers: Covers = Covers()
    val creator: String = ""

    @JsonProperty("favourite_count")
    val favouriteCount: Int = 0
    val id: Int = 0
    val nsfw: Boolean = false

    @JsonProperty("play_count")
    val playCount: Int = 0

    @JsonProperty("preview_url")
    var previewUrl: String = "https://b.ppy.sh/preview/$id"
        get() = "https:$field"
    val status: String = ""
    val title: String = ""

    @JsonProperty("title_unicode")
    val titleUnicode: String? = null

    @JsonProperty("user_id")
    val userId: Int = 0
    val video: Boolean = false

    // Optional fields
    val beatmaps: List<Beatmap>? = null
    val description: String? = null
    val ratings: Array<Int>? = null
    val offset: Int = 0
    @JsonProperty("has_favourited")
    val favourited: Boolean? = null

    class Covers : Serializable {
        val cover: String = ""

        @JsonProperty("cover@2x")
        val cover2x: String = ""
        val card: String = ""

        @JsonProperty("card@2x")
        val card2x: String = ""
        val list: String = ""

        @JsonProperty("list@2x")
        val list2x: String = ""
        val slimcover: String = ""

        @JsonProperty("slimcover@2x")
        val slimcover2x: String = ""
    }
}
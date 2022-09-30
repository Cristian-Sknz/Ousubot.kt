package me.sknz.ousubot.api.models.users

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

open class UserCompact: Serializable {
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

    fun getUrl(): String {
        return "https://osu.ppy.sh/users/$id"
    }
}
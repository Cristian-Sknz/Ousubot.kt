package me.sknz.ousubot.app.api.models.enums

import com.fasterxml.jackson.annotation.JsonProperty

enum class GameMode(val id: Int, val mode: String) {

    @JsonProperty("fruits")
    Fruits(3,"osu!catch"),
    @JsonProperty("mania")
    Mania(2, "osu!mania"),
    @JsonProperty("osu")
    Osu(0,"osu!standard"),
    @JsonProperty("taiko")
    Taiko(1, "osu!taiko");

    override fun toString(): String {
        return this.name
    }

    companion object {
        fun of(mode: String?) = GameMode.values().find { it.mode.equals(mode, true) }
    }
}

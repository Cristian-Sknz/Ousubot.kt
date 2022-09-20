package me.sknz.ousubot.api.models.enums

import com.fasterxml.jackson.annotation.JsonProperty

enum class GameMode(val mode: String) {
    @JsonProperty("fruits")
    Fruits("osu!catch"),
    @JsonProperty("mania")
    Mania("osu!mania"),
    @JsonProperty("osu")
    Osu("osu!standard"),
    @JsonProperty("taiko")
    Taiko("osu!taiko");
}

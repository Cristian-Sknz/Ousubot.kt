package me.sknz.ousubot.api.models

import java.time.OffsetDateTime

data class Beatmap(
    val accuracy: Float = 0F,
    val ar: Float = 0F,
    val beatmapset_id: Int = 0,
    val bpm: Float = 0F,
    val convert: Boolean = false,
    val count_circles: Int = 0,
    val count_sliders: Int = 0,
    val count_spinners: Int = 0,
    val cs: Float = 0F,
    val deleted_at: OffsetDateTime? = null,
    val drain: Float = 0F,
    val hit_length: Int = 0,
    val is_scoreable: Boolean = false,
    val last_updated: OffsetDateTime? = null,
    val mode_int: Int = 0,
    val passcount: Int = 0,
    val playcount: Int = 0,
    val ranked: Int = 0,
    val url: String = ""
) {
}
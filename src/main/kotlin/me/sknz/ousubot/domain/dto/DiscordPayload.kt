package me.sknz.ousubot.domain.dto

import com.fasterxml.jackson.databind.ObjectMapper

data class DiscordPayload<T>(
    val next: Long?,
    val back: Long?,
    val payload: T?
) {

    constructor(adb: AbstractDiscordEmbed<T>) : this(adb.next, adb.back, adb.payload)
    constructor(): this(null, null, null)

    companion object {
        private val mapper = ObjectMapper()
        
        @Suppress("UNCHECKED_CAST")
        fun <T> from(value: String): DiscordPayload<T>{
            return mapper.readValue(value, DiscordPayload::class.java) as DiscordPayload<T>
        }
    }
    
    fun json(): String {
        return mapper.writeValueAsString(this)
    }
}
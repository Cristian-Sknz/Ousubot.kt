package me.sknz.ousubot.infrastructure.events.interactions

enum class IDType {
    EQUALS,
    STARTS_WITH,
    ENDS_WITH;

    fun isValidId(id: String, value: String): Boolean {
        return when (this) {
            EQUALS -> value.equals(id, true)
            STARTS_WITH -> value.startsWith(id, true)
            ENDS_WITH -> value.endsWith(id, true)
        }
    }
}
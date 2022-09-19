package me.sknz.ousubot.core.commands.autocomplete

import net.dv8tion.jda.api.interactions.commands.Command

data class CommandChoice<T>(val name: String, val value: T) {

    fun toCommandChoice(): Command.Choice {
        return when (value) {
            is String -> Command.Choice(name, value)
            is Number -> {
                when (value) {
                    is Long -> Command.Choice(name, value)
                    is Double -> Command.Choice(name, value)
                    else -> Command.Choice(name, value.toLong())
                }
            }
            else -> Command.Choice(name, value.toString())
        }
    }
}
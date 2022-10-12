package me.sknz.ousubot.infrastructure.events.commands.custom

import kotlin.reflect.KFunction

class CommandFunction(
    val instance: Any,
    val function: KFunction<*>
) {

    fun call(vararg args: Any?): Any? {
        return function.call(instance, *args)
    }
}
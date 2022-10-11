package me.sknz.ousubot.core.commands.custom

import kotlin.reflect.KFunction

class CommandFunction(
    val instance: Any,
    val function: KFunction<*>
) {

    fun call(vararg args: Any?): Any? {
        return function.call(instance, *args)
    }
}
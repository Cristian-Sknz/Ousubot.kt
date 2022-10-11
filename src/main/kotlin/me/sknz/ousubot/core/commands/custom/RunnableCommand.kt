package me.sknz.ousubot.core.commands.custom

interface RunnableCommand {

    var function: CommandFunction

    fun call(vararg args: Any?): Any? {
        return function.call(*args)
    }
}
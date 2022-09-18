package me.sknz.ousubot.core.commands

import me.sknz.ousubot.core.commands.autocomplete.CommandAutoComplete
import org.springframework.context.ApplicationContext
import kotlin.reflect.KClass

data class LinkedOption(val name: String, val complete: KClass<CommandAutoComplete<*>>) {

    fun getAutoComplete(context: ApplicationContext): CommandAutoComplete<*> {
        return context.getBean(complete.java)
    }
}
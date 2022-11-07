package me.sknz.ousubot.infrastructure.tools

import java.util.*

fun String.toCamelCase(): String {
    return "(?<=[a-zA-Z])[A-Z]".toRegex()
        .replace(this, ) { "_${it.value}" }
        .lowercase(Locale.getDefault())
}
package me.sknz.ousubot.infrastructure.tools

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST")
class InitOnceProperty<T> : ReadWriteProperty<Any, T> {

    private object EMPTY
    private var value: Any? = EMPTY

    companion object {
        inline fun <reified T> initOnce(): ReadWriteProperty<Any, T> = InitOnceProperty()
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        if (value == EMPTY) {
            throw IllegalStateException("Value isn't initialized")
        } else {
            return value as T
        }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        if (this.value != EMPTY) {
            throw IllegalStateException("Value is initialized")
        }
        this.value = value
    }
}



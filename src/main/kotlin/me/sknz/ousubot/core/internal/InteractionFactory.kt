package me.sknz.ousubot.core.internal

import kotlin.reflect.KClass

interface InteractionFactory<T> {

    fun create(instance: Any, clazz: KClass<out Any>, vararg params: Any?): T
}
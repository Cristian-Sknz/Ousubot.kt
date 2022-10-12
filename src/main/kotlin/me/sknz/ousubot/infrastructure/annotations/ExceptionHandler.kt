package me.sknz.ousubot.infrastructure.annotations

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ExceptionHandler(
    val value: KClass<out Throwable>,
)

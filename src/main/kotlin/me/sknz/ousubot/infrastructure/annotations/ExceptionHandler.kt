package me.sknz.ousubot.infrastructure.annotations

import kotlin.reflect.KClass
import me.sknz.ousubot.infrastructure.exceptions.AbstractExceptionHandler

/**
 * ## ExceptionHandler
 *
 * Anotação para declarar uma função como um Manipulador de Exceções.
 *
 * @see AbstractExceptionHandler
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class ExceptionHandler(
    val value: KClass<out Throwable>,
)

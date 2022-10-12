package me.sknz.ousubot.infrastructure.annotations

/**
 * ## WorkInProgress
 *
 * Anotação para avisar que uma feature está em desenvolvimento e ainda não está completa.
 */
@Retention(AnnotationRetention.SOURCE)
annotation class WorkInProgress(val message: String = "")

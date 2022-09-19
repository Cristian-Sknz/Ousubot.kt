package me.sknz.ousubot.api.annotations

/**
 * ## WorkInProgress
 *
 * Anotação para avisar que uma feature está em desenvolvimento e ainda não está completa.
 */
@Retention(AnnotationRetention.SOURCE)
annotation class WorkInProgress(val message: String = "")

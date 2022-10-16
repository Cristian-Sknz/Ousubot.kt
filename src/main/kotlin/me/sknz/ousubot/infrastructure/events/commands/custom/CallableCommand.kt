package me.sknz.ousubot.infrastructure.events.commands.custom

import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import kotlin.reflect.*

/**
 * ## CallableCommand
 *
 * Instancia que efetuará a chamada de um [SlashCommandData].
 */
interface CallableCommand: KCallable<Any?> {

    val function: KFunction<*>
    val instance: Any

    override val annotations: List<Annotation>
        get() = this.function.annotations
    override val isAbstract: Boolean
        get() = this.function.isAbstract
    override val isFinal: Boolean
        get() = this.function.isFinal
    override val isOpen: Boolean
        get() = this.function.isOpen
    override val isSuspend: Boolean
        get() = this.function.isOpen
    override val parameters: List<KParameter>
        get() = this.function.parameters
    override val returnType: KType
        get() = this.function.returnType
    override val typeParameters: List<KTypeParameter>
        get() = this.function.typeParameters
    override val visibility: KVisibility?
        get() = this.function.visibility

    override fun call(vararg args: Any?): Any? = function.call(instance, *args)
    override fun callBy(args: Map<KParameter, Any?>): Nothing = TODO("Not yet implemented")
}
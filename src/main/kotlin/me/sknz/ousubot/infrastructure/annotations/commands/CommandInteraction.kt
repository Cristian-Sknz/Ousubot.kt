package me.sknz.ousubot.infrastructure.annotations.commands


/**
 * Anotação para declarar uma função/método como um SlashCommand
 * Ao declarar uma função como SlashCommand, ela começa a se comportar
 * como um comando que poderá ser chamado pelo bot.
 *
 * Se a classe onde este comando está, estiver com a propriedade [SlashCommandController.name]
 * este comando irá fazer parte de um comando como um subcomando.
 *
 * @see SlashCommandController
 * @see SlashCommandOptions
 */
@Retention(value = AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class SlashCommand(val name: String,
                              val description: String = "Not specified")
annotation class MessageInteraction(val name: String)
annotation class UserInteraction(val name: String)
package me.sknz.ousubot.infrastructure.events.commands

import me.sknz.ousubot.infrastructure.annotations.commands.*
import me.sknz.ousubot.infrastructure.events.commands.autocomplete.CommandAutoComplete
import me.sknz.ousubot.infrastructure.events.commands.autocomplete.NumberAutoComplete
import me.sknz.ousubot.infrastructure.events.commands.autocomplete.StringAutoComplete
import me.sknz.ousubot.infrastructure.events.commands.custom.CustomOption
import me.sknz.ousubot.infrastructure.events.commands.custom.CustomSlashCommandData
import me.sknz.ousubot.infrastructure.events.commands.exception.CommandRegisterException
import me.sknz.ousubot.infrastructure.tools.DiscordI18nBundle
import me.sknz.ousubot.infrastructure.events.InteractionFactory
import net.dv8tion.jda.api.interactions.commands.Command
import net.dv8tion.jda.api.interactions.commands.OptionType
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf

class CommandFunctionFactory : InteractionFactory<CustomSlashCommandData> {

    override fun create(instance: Any, clazz: KClass<out Any>, vararg params: Any?): List<CustomSlashCommandData> {
        val controller = clazz.findAnnotation<SlashCommandController>()
            ?: throw CommandRegisterException("A Classe ${clazz.simpleName} não está anotada com a anotação SlashCommandController")
        val functions = clazz.declaredFunctions.filter {
            it.hasAnnotation<SlashCommand>() || it.hasAnnotation<UserInteraction>() || it.hasAnnotation<MessageInteraction>()
        }.ifEmpty { throw CommandRegisterException("A Classe ${clazz.simpleName} não contém SlashCommands") }

        val interactions = interactions(instance, functions)

        if (controller.name.isNotEmpty()) {
            return listOf(subcommands(instance, functions, controller), interactions).flatten()
        }
        val commands = arrayListOf<CustomSlashCommandData>()

        for (function in functions) {
            val annotation = function.findAnnotation<SlashCommand>() ?: continue
            val command = CustomSlashCommandData(annotation.name, annotation.description)
                .setFunction(instance, function)
                .addOptions(getCommandOptions(function))
                .setLocalizationFunction(DiscordI18nBundle)

            commands.add(command as CustomSlashCommandData)
        }
        return listOf(commands, interactions).flatten()
    }

    private fun subcommands(instance: Any, functions: List<KFunction<*>>, controller: SlashCommandController): List<CustomSlashCommandData> {
        val command = CustomSlashCommandData(controller.name)
            .setLocalizationFunction(DiscordI18nBundle)
        for (function in functions) {
            val annotation = function.findAnnotation<SlashCommand>() ?: continue
            val subcommand = CustomSlashCommandData.SlashSubcommandData(annotation.name, annotation.description)
                .setFunction(instance, function)
                .addOptions(getCommandOptions(function))

            command.addSubcommands(subcommand)
        }

        if (command.subcommands.isEmpty()) {
            return emptyList()
        }
        return listOf(command as CustomSlashCommandData)
    }

    private fun interactions(instance: Any, functions: List<KFunction<*>>): List<CustomSlashCommandData> {
        return functions.map { function ->
            val annotation = function.findAnnotation<UserInteraction>()
                ?: function.findAnnotation<MessageInteraction>() ?: return@map null

            if (annotation is UserInteraction) {
                return@map CustomSlashCommandData(Command.Type.USER, annotation.name)
                    .setFunction(instance, function)
                    .setLocalizationFunction(DiscordI18nBundle)
                as CustomSlashCommandData
            }
            return@map CustomSlashCommandData(Command.Type.MESSAGE, (annotation as MessageInteraction).name)
                .setFunction(instance, function)
                .setLocalizationFunction(DiscordI18nBundle)
            as CustomSlashCommandData
        }.filterNotNull()
    }

    private fun getCommandOptions(function: KFunction<*>): List<CustomOption> {
        val options = function.findAnnotation<SlashCommandOptions>()?.value
            ?: arrayOf(function.findAnnotation<SlashCommandOption>() ?: return emptyList())

        return options.map { option ->
            if (option.isCommandAutoComplete()) {
                return@map CustomOption(option.type, option.name, option.description,
                    option.autocomplete, option.required)
            }
            return@map CustomOption(option.type, option.name, option.description,
                null, option.required)
        }
    }

    /**
     * Verificar se a classe de auto-complete é valida.
     */
    private fun SlashCommandOption.isCommandAutoComplete(): Boolean {
        return when {
            this.autocomplete.isSubclassOf(CommandAutoComplete::class) -> {
                when {
                    this.autocomplete.isSubclassOf(StringAutoComplete::class) && this.type == OptionType.STRING -> true
                    this.autocomplete.isSubclassOf(NumberAutoComplete::class) && this.type == OptionType.INTEGER -> true
                    this.autocomplete.isSubclassOf(NumberAutoComplete::class) && this.type == OptionType.NUMBER -> true
                    else -> false
                }
            }
            else -> false
        }
    }

}
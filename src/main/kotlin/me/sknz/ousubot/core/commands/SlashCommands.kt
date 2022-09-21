package me.sknz.ousubot.core.commands

import me.sknz.ousubot.core.annotations.commands.SlashCommand
import me.sknz.ousubot.core.annotations.commands.SlashCommandController
import me.sknz.ousubot.core.annotations.commands.SlashCommandOption
import me.sknz.ousubot.core.annotations.commands.SlashCommandOptions
import me.sknz.ousubot.core.commands.autocomplete.CommandAutoComplete
import me.sknz.ousubot.core.commands.autocomplete.NumberAutoComplete
import me.sknz.ousubot.core.commands.autocomplete.StringAutoComplete
import me.sknz.ousubot.core.commands.exception.CommandRegisterException
import me.sknz.ousubot.core.commands.tools.CommandParameterInjector
import me.sknz.ousubot.core.context.SlashCommandContext
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.SubscribeEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData
import net.dv8tion.jda.api.requests.RestAction
import org.apache.logging.log4j.LogManager
import org.springframework.context.ApplicationContext
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf

/**
 * SlashCommands responsável em armazenar e registrar comandos.
 * Está classe é uma classe interna e não deve ser modificada externamente!
 *
 * Essa classe é chamada ao configurar o [net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder]
 * em [me.sknz.ousubot.core.JDAConfiguration]
 *
 * @param context Contexto da aplicação Spring Boot
 * @see SlashCommands
 */
internal class SlashCommands(
    private val context: ApplicationContext,
) {
    private val map: HashMap<String, LinkedSlashCommand> = hashMapOf()
    private val logger = LogManager.getLogger(this::class.java)
    private val injector = CommandParameterInjector(context)

    @SubscribeEvent
    fun slashCommand(event: SlashCommandInteractionEvent) {
        context.getBean(SlashCommandContext::class.java).event = event
        if (event.name.lowercase() !in this.map) {
            event.reply("Este comando não está disponível no bot.")
                .setEphemeral(true)
                .queue()
            return
        }

        val value = this.map[event.name.lowercase()]!!
        value.getFunction(event.subcommandName)?.let {
            val params = injector.getInitializedParameters(event, it)
            val callback = it.call(value.instance, *params.toTypedArray())
            if (callback is RestAction<*>) callback.queue()
        }
    }

    @SubscribeEvent
    fun autocomplete(event: CommandAutoCompleteInteractionEvent) {
        val value = this.map[event.name.lowercase()] ?: return
        val focused = event.focusedOption

        value.getLinkedSlashCommand(event.subcommandName)?.let {
            val complete = it.options?.find { option -> option.name == focused.name }
                ?.getAutoComplete(context)
            if (complete is StringAutoComplete || complete is NumberAutoComplete) {
                event.replyChoices(complete.onAutoComplete(focused.value).map { value -> value.toCommandChoice() })
                    .queue()
            }
        }
    }

    /**
     * Função para registrar comando(s) de uma instância de um [SlashCommandController].
     * Está função por baixo dos panos, utiliza reflexão para manipular as anotações
     *
     * Esta função também registra SubComandos [SlashCommandController.name]
     *
     * @param instance Uma classe instanciada de [SlashCommandController]
     * @see SlashCommandController
     */
    fun register(instance: Any) {
        val clazz = instance::class
        val annotation = clazz.findAnnotation<SlashCommandController>()
            ?: throw CommandRegisterException("A Classe ${clazz.simpleName} não foi declarada como um SlashCommandController")

        if (annotation.name.isEmpty()) {
            registerCommand(instance, clazz)
            return
        }
        registerCommandWithSubCommands(instance, clazz, annotation)
    }

    /**
     * Função para atualizar os comandos registrados nesta classe e
     * enviar para a API do Discord.
     *
     * Esta função ira apagar todos os comandos que não estão inclusos nesta classe
     * e utiliza o [JDA.upsertCommand] para registrar novos comandos.
     *
     * @param jda Uma instância do [JDA]
     * @see JDA.upsertCommand
     */
    fun update(jda: JDA) {
        jda.retrieveCommands().complete().forEach {
            if (!map.containsKey(it.name)) it.delete().complete()
        }
        map.values.forEach {
            if (it.command is SlashCommandData) jda.upsertCommand(it.command).queue()
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun registerCommand(instance: Any, clazz: KClass<out Any>) {
        val functions = clazz.declaredFunctions.filter { it.hasAnnotation<SlashCommand>() }
        val commands = arrayListOf<LinkedSlashCommand>()

        for (function in functions) {
            val command = function.findAnnotation<SlashCommand>()?.toCommand() ?: continue

            // Pegar todas as opções com autocomplete
            val options = let {
                val values = arrayListOf<LinkedOption>()
                function.findAnnotation<SlashCommandOptions>()?.let {
                    for (option in it.value) {
                        if (!option.isCommandAutoComplete()) {
                            continue
                        }

                        values.add(LinkedOption(option.name, option.autocomplete as KClass<CommandAutoComplete<*>>))
                    }
                    command.addOptions(it)
                } ?: function.findAnnotation<SlashCommandOption>()?.let {
                    if (it.isCommandAutoComplete()) {
                        values.add(LinkedOption(it.name, it.autocomplete as KClass<CommandAutoComplete<*>>))
                    }
                    command.addOption(it)
                }
                values
            }
            commands.add(LinkedSlashCommand(instance, command, null, function, options))
        }
        commands.forEach {
            this.map[it.getName()] = it
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun registerCommandWithSubCommands(
        instance: Any,
        clazz: KClass<out Any>,
        annotation: SlashCommandController
    ) {
        val functions = clazz.declaredFunctions.filter { it.hasAnnotation<SlashCommand>() }
        if (functions.isEmpty()) {
            logger.warn("A Class ${clazz.simpleName} foi declarada como uma SlashCommand, mas não possui SubComandos.")
            return
        }

        val command = Commands.slash(annotation.name, "Not Specified")
        val subcommands = arrayListOf<LinkedSlashCommand>()
        for (function in functions) {
            val subcommand = function.findAnnotation<SlashCommand>()!!.toSubCommand()

            // Pegar todas as opções com autocomplete
            val options = let {
                val values = arrayListOf<LinkedOption>()
                function.findAnnotation<SlashCommandOptions>()?.let {
                    for (option in it.value) {
                        if (!option.isCommandAutoComplete()) {
                            continue
                        }

                        values.add(LinkedOption(option.name, option.autocomplete as KClass<CommandAutoComplete<*>>))
                    }
                    subcommand.addOptions(it)
                } ?: function.findAnnotation<SlashCommandOption>()?.let {
                    if (it.isCommandAutoComplete()) {
                        values.add(LinkedOption(it.name, it.autocomplete as KClass<CommandAutoComplete<*>>))
                    }
                    subcommand.addOption(it)
                }
                values
            }

            command.addSubcommands(subcommand)
            subcommands.add(LinkedSlashCommand(instance, subcommand, null, function, options))
        }
        this.map[command.name] = LinkedSlashCommand(instance, command, subcommands, null, null)
        return
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

    private fun SlashCommand.toCommand(): SlashCommandData {
        return Commands.slash(this.name, this.description)
    }

    private fun SlashCommand.toSubCommand(): SubcommandData {
        return SubcommandData(this.name, this.description)
    }

    private fun SlashCommandData.addOption(option: SlashCommandOption): SlashCommandData {
        return this.addOption(
            option.type,
            option.name,
            option.description,
            option.required,
            option.isCommandAutoComplete()
        )
    }

    private fun SlashCommandData.addOptions(slashCommandOptions: SlashCommandOptions): SlashCommandData {
        for (option in slashCommandOptions.value) {
            this.addOption(option)
        }
        return this
    }

    private fun SubcommandData.addOption(option: SlashCommandOption): SubcommandData {
        return this.addOption(
            option.type,
            option.name,
            option.description,
            option.required,
            option.isCommandAutoComplete()
        )
    }

    private fun SubcommandData.addOptions(slashCommandOptions: SlashCommandOptions): SubcommandData {
        for (option in slashCommandOptions.value) {
            this.addOption(option)
        }
        return this
    }
}






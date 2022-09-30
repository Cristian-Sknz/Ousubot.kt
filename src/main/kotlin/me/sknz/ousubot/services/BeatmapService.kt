package me.sknz.ousubot.services

import me.sknz.ousubot.dto.BeatmapSetRequest
import me.sknz.ousubot.dto.DiscordBeatmapEmbed

/**
 * ## BeatmapService
 *
 * Classe responsável pelas regras de negócio envolvendo requisições
 * de SlashCommands para a API de beatmap do Osu!
 *
 * @see me.sknz.ousubot.commands.BeatmapController
 */
interface BeatmapService<T> : CachedService<T> {

    /**
     * Função para pegar/gerar uma mensagem prontas.
     *
     * Por baixo dos panos essa função utiliza de cache em memória
     * para otimizar o processamento de uma requisição e evitar
     * requisições desnecessárias na API do Osu
     *
     * @param request Uma requisição de beatmap ou beatmapSet
     */
    fun getBeatmapEmbed(request: BeatmapSetRequest): DiscordBeatmapEmbed
}
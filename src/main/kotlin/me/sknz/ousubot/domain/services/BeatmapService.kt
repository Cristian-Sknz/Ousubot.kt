package me.sknz.ousubot.domain.services

import me.sknz.ousubot.app.commands.BeatmapController
import me.sknz.ousubot.domain.dto.BeatmapSetRequest
import me.sknz.ousubot.domain.dto.DiscordBeatmapEmbed
import me.sknz.ousubot.domain.services.type.CachedService
import me.sknz.ousubot.domain.services.type.OsuAPIService

/**
 * ## BeatmapService
 *
 * Classe responsável pelas regras de negócio envolvendo requisições
 * de SlashCommands para a API de beatmap do Osu!
 *
 * @see BeatmapController
 */
interface BeatmapService<T: BeatmapService<T>> : CachedService<T>, OsuAPIService {

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
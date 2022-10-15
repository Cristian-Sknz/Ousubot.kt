package me.sknz.ousubot.domain.services.impl

import me.sknz.ousubot.app.api.OsuClientAPI
import me.sknz.ousubot.app.api.models.scores.BeatmapScore
import me.sknz.ousubot.app.api.models.scores.BeatmapScores
import me.sknz.ousubot.domain.dto.DiscordScoreEmbed
import me.sknz.ousubot.domain.dto.ScoreRequest
import me.sknz.ousubot.domain.services.ScoreService
import me.sknz.ousubot.domain.utils.template
import me.sknz.ousubot.infrastructure.annotations.WorkInProgress
import me.sknz.ousubot.infrastructure.xml.DiscordEmbed
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.thymeleaf.spring5.SpringTemplateEngine
import java.util.*

@WorkInProgress
@Service
class ScoreServiceImpl(
    override val client: OsuClientAPI,
    override val engine: SpringTemplateEngine,
    val beatmapService: BeatmapServiceImpl
) : ScoreService<ScoreServiceImpl> {

    @Autowired
    @Lazy
    override lateinit var self: ScoreServiceImpl

    override fun getBeatmapScoreEmbed(request: ScoreRequest): DiscordScoreEmbed {
        val scores = self.getBeatmapScore(request.beatmap)
        if (request.score == null) {
            return self.getScoreEmbed(ScoreRequest(request.beatmap, scores[0].id, request.locale), scores)
        }

        return self.getScoreEmbed(request, scores)
    }

    @Cacheable(cacheNames = ["embed:beatmapscore"], key = "#request")
    fun getScoreEmbed(request: ScoreRequest, beatmapScores: BeatmapScores): DiscordScoreEmbed {
        val scores = beatmapScores.scores
        for ((index, score) in scores.withIndex()) {
            if (score.id != request.score) {
                continue
            }
            val embed = process(score, request.locale)
            return DiscordScoreEmbed(embed,
                scores.getOrNull(index + 1)?.id,
                scores.getOrNull(index - 1)?.id,
                score.beatmap!!.id)
        }

        throw RuntimeException("Este beatmapId não faz parte deste BeatmapSet")
    }

    @Cacheable(cacheNames = ["api:beatmapscore"], key = "#beatmapId")
    fun getBeatmapScore(beatmapId: Int): BeatmapScores {
        val beatmapSet = beatmapService.getBeatmapSetByBeatmapId(beatmapId)

        val score = client.getBeatmapScore(beatmapId)
        score.scores.forEach {
            it.beatmap = beatmapSet[beatmapId]
            it.beatmap!!.beatmapSet = beatmapSet
        }

        return score
    }

    fun process(score: BeatmapScore, locale: Locale): DiscordEmbed {
        return template(engine) {
            this.template = "BeatmapScoreEmbed"
            this.language = locale

            this.variables["score"] = score
            this.variables["beatmap"] = score.beatmap!!
        }
    }

}
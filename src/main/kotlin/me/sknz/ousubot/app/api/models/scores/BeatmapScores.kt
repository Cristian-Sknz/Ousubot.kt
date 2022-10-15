package me.sknz.ousubot.app.api.models.scores

import java.io.Serializable

class BeatmapScores : Iterable<BeatmapScore>, Serializable {

    val scores: Array<BeatmapScore> = emptyArray()

    operator fun get(index: Int): BeatmapScore {
        return scores[index]
    }
    override fun iterator(): Iterator<BeatmapScore> {
        return scores.iterator()
    }
}

package me.sknz.ousubot.app.api.models.scores

import java.io.Serializable

class BeatmapScores(
    val scores: Array<BeatmapScore> = emptyArray()
) : Iterable<BeatmapScore>, Serializable {

    fun isEmpty() = scores.isEmpty()
    fun isNotEmpty() = scores.isNotEmpty()

    operator fun get(index: Int): BeatmapScore {
        return scores[index]
    }
    override fun iterator(): Iterator<BeatmapScore> {
        return scores.iterator()
    }
}

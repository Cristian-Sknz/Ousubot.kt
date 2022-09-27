package me.sknz.ousubot.api

import me.sknz.ousubot.api.models.beatmaps.Beatmap
import me.sknz.ousubot.api.models.beatmaps.BeatmapSearch
import me.sknz.ousubot.api.models.beatmaps.BeatmapSet
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@FeignClient(name="osu", url = "https://osu.ppy.sh/api/v2")
interface OsuClientAPI {

    @RequestMapping(method = [RequestMethod.GET], value= ["/beatmaps/{beatmapId}"])
    fun getBeatmap(@PathVariable("beatmapId") id: Int): Beatmap

    @RequestMapping(method = [RequestMethod.GET], value= ["/beatmapsets/lookup?beatmap_id={beatmapId}"])
    fun lockupBeatmapSet(@PathVariable("beatmapId") id: Int): BeatmapSet

    @RequestMapping(method = [RequestMethod.GET], value= ["/beatmapsets/{beatmapSetId}"])
    fun getBeatmapSet(@PathVariable("beatmapSetId") id: Int): BeatmapSet

    @RequestMapping(method = [RequestMethod.GET], value= ["/beatmapsets/search/?q={query}"])
    fun searchBeatmap(@PathVariable("query") query: String): BeatmapSearch
}
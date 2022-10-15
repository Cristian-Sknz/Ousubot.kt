package me.sknz.ousubot.app.api

import me.sknz.ousubot.app.api.models.beatmaps.Beatmap
import me.sknz.ousubot.app.api.models.beatmaps.BeatmapSearch
import me.sknz.ousubot.app.api.models.beatmaps.BeatmapSet
import me.sknz.ousubot.app.api.models.scores.BeatmapScores
import me.sknz.ousubot.app.api.models.users.User
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

    @RequestMapping(method = [RequestMethod.GET], value= ["/users/{user}"])
    fun getUser(@PathVariable("user") user: String): User

    @RequestMapping(method = [RequestMethod.GET], value = ["/beatmaps/{beatmap}/scores"])
    fun getBeatmapScore(@PathVariable("beatmap") beatmapId: Int): BeatmapScores
}
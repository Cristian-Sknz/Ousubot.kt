package me.sknz.ousubot.api

import me.sknz.ousubot.api.models.Beatmap
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@FeignClient(name="osu", url = "https://osu.ppy.sh/api/v2")
interface OsuClientAPI {

    @RequestMapping(method = [RequestMethod.GET], value= [" /beatmaps/{id}"])
    fun getBeatmap(@PathVariable("id") id: Int): ArrayList<Beatmap>
}
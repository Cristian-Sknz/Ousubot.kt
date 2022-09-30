package me.sknz.ousubot.core.exceptions

import feign.FeignException.NotFound
import feign.Request
import feign.RequestTemplate

fun osuNotFound(message: String): Nothing = throw NotFound(message, getEmptyRequest(), Request.Body.empty().asBytes(), hashMapOf())

private fun getEmptyRequest(): Request {
    return Request.create(Request.HttpMethod.TRACE, "", hashMapOf(), null, RequestTemplate())
}
package me.sknz.ousubot.api.adapter

import org.springframework.data.jpa.repository.JpaRepository

interface OsuTokenRepository : JpaRepository<OsuAccessToken, Int>
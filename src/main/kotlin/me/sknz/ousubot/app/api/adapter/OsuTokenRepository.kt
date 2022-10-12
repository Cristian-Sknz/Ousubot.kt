package me.sknz.ousubot.app.api.adapter

import org.springframework.data.jpa.repository.JpaRepository

interface OsuTokenRepository : JpaRepository<OsuAccessToken, Int>
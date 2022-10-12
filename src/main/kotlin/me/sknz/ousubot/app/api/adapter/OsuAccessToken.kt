package me.sknz.ousubot.app.api.adapter

import java.time.OffsetDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.Table

@Entity
@Table(name = "osu_access_token")
open class OsuAccessToken {

    @Id
    @Column(name = "id", nullable = false)
    open var id: Int? = null
    @Lob
    @Column(columnDefinition = "text")
    open var accessToken: String? = null

    @Lob
    @Column(columnDefinition = "text")
    open var refreshToken: String? = null
    open var createdDate: OffsetDateTime? = null
    open var expireDate: OffsetDateTime? = null
}
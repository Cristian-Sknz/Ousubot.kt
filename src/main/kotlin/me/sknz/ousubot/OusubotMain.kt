package me.sknz.ousubot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class OusubotMain

fun main(args: Array<String>) {
    runApplication<OusubotMain>(*args)
}
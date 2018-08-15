package com.github.kpavlov.txservice

import com.github.kpavlov.txservice.ws.RestServer

fun main(args: Array<String>) {
    val restServer = RestServer()
    restServer.start()
}
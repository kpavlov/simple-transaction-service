package com.github.kpavlov.txservice.ws

import io.netty.channel.Channel
import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.netty.httpserver.NettyHttpContainerProvider
import org.glassfish.jersey.server.ResourceConfig
import java.net.URI


class RestServer {

    private val BASE_URI = URI.create("http://localhost:8080/")

    private lateinit var server: Channel

    fun start() {
        val resourceConfig = ResourceConfig(
                AccountsResource::class.java,
                TransactionsResource::class.java)
        resourceConfig.register(JacksonFeature::class.java)

        server = NettyHttpContainerProvider.createHttp2Server(BASE_URI, resourceConfig, null)

        Runtime.getRuntime().addShutdownHook(Thread(Runnable { stop() }))
    }

    fun stop() {
        server.close().await()
    }
}


package com.github.kpavlov.txservice.ws

import io.netty.channel.Channel
import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.netty.httpserver.NettyHttpContainerProvider
import org.glassfish.jersey.server.ResourceConfig
import org.slf4j.LoggerFactory
import java.net.URI


class RestServer(host: String = "localhost", port: Int = 8080) {

    private val BASE_URI = URI.create("http://$host:$port/")

    private val logger = LoggerFactory.getLogger(RestServer::class.java)

    private lateinit var server: Channel

    fun start() {
        val resourceConfig = ResourceConfig(
                AccountsResource::class.java,
                TransactionsResource::class.java)
        resourceConfig.register(JacksonFeature::class.java)

        logger.info("Starting HTTP server on {}", BASE_URI)
        server = NettyHttpContainerProvider.createHttp2Server(BASE_URI, resourceConfig, null)
        logger.info("HTTP server started")

        Runtime.getRuntime().addShutdownHook(Thread(Runnable { stop() }))
    }

    fun stop() {
        logger.info("Stopping HTTP server...")
        server.close().await()
        logger.info("HTTP server stopped")
    }
}


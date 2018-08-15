package com.github.kpavlov.txservice.ws

import com.sun.net.httpserver.HttpServer
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory
import org.slf4j.LoggerFactory
import javax.ws.rs.core.UriBuilder

class RestServer(host: String = "localhost", port: Int = 8080) {

    private val BASE_URI = UriBuilder.fromUri("http://$host/").port(port).build()

    private val logger = LoggerFactory.getLogger(RestServer::class.java)

    private lateinit var server: HttpServer

    fun start() {
        val resourceConfig = JerseyApplication()

        logger.info("Starting HTTP server on {}", BASE_URI)
        server = JdkHttpServerFactory.createHttpServer(BASE_URI, resourceConfig)
        logger.info("HTTP server started")

        Runtime.getRuntime().addShutdownHook(Thread(Runnable { stop() }))
    }

    fun stop() {
        logger.info("Stopping HTTP server...")
        server.stop(1)
        logger.info("HTTP server stopped")
    }
}


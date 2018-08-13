package com.github.kpavlov.txservice.it

import com.github.kpavlov.txservice.ws.RestServer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll

abstract class AbstractIT {

    @Suppress("unused")
    companion object {

        private val server = RestServer()

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            server.start()
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            server.stop()
        }
    }

}
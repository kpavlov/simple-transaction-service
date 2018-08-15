package com.github.kpavlov.txservice.ws

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.kpavlov.txservice.service.AccountServiceImpl
import com.github.kpavlov.txservice.service.TransactionServiceImpl
import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.server.ServerProperties
import javax.ws.rs.ext.ContextResolver

class JerseyApplication : ResourceConfig() {

    private val accountService = AccountServiceImpl()
    private val transactionService = TransactionServiceImpl(accountService)

    init {
        registerInstances(
                AccountsResource(accountService),
                TransactionsResource(transactionService)
        )
        register(JacksonFeature::class.java)
        register(ContextResolver<ObjectMapper> {
            ObjectMapper()
                    .findAndRegisterModules()
                    .registerModule(KotlinModule())
                    .setDefaultPropertyInclusion(JsonInclude.Include.NON_EMPTY)
        })
        property(ServerProperties.WADL_FEATURE_DISABLE, true)
    }

}
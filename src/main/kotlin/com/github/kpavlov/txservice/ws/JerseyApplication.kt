package com.github.kpavlov.txservice.ws

import com.github.kpavlov.txservice.service.AccountServiceImpl
import com.github.kpavlov.txservice.service.TransactionServiceImpl
import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.server.ServerProperties

class JerseyApplication : ResourceConfig() {

    private val accountService = AccountServiceImpl()
    private val transactionService = TransactionServiceImpl(accountService)

    init {
        registerInstances(
                AccountsResource(accountService),
                TransactionsResource(transactionService)
        )
        register(ObjectMapperProvider::class)
        register(JacksonFeature::class.java)
        property(ServerProperties.WADL_FEATURE_DISABLE, true)
    }

}
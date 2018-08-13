package com.github.kpavlov.txservice.ws

import com.github.kpavlov.txservice.service.AccountServiceImpl
import com.github.kpavlov.txservice.service.TransactionServiceImpl
import javax.ws.rs.core.Application

class JerseyApplication : Application() {

    private val accountService = AccountServiceImpl()
    private val transactionService = TransactionServiceImpl(accountService)

    override fun getSingletons(): Set<Any> {
        return setOf(
                AccountsResource(accountService),
                TransactionsResource(transactionService)
        )
    }

}
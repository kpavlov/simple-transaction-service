package com.github.kpavlov.txservice.ws

import com.github.kpavlov.txservice.service.AccountService
import com.github.kpavlov.txservice.ws.model.AccountDetails
import com.github.kpavlov.txservice.ws.model.CreateAccountRequest
import java.math.BigDecimal
import java.net.URI
import javax.ws.rs.core.Response


class AccountsResource(private val accountService: AccountService) : AccountsApi() {

    override fun createAccount(request: CreateAccountRequest): Response {
        val account = accountService.createAccount(request.initialBalance.movePointRight(2).toInt())

        val responseBody = with(AccountDetails()) {
            id = account.id
            balance = BigDecimal.valueOf(account.getBalance().toLong()).movePointLeft(2)
            this
        }

        return Response.created(URI("/accounts/${account.id}"))
                .entity(responseBody).build()
    }

}
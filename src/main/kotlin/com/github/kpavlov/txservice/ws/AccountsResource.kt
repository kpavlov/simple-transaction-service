package com.github.kpavlov.txservice.ws

import com.github.kpavlov.txservice.domain.Account
import com.github.kpavlov.txservice.service.AccountService
import com.github.kpavlov.txservice.ws.model.AccountDetails
import com.github.kpavlov.txservice.ws.model.CreateAccountRequest
import java.math.BigDecimal
import java.net.URI
import javax.ws.rs.core.Response


class AccountsResource(private val accountService: AccountService) : AccountsApi() {

    override fun createAccount(request: CreateAccountRequest): Response {
        val account = accountService.createAccount(request.initialBalance.movePointRight(2).toInt())

        val responseBody = createAccountDetails(account)

        return Response.created(URI("/accounts/${account.id}"))
                .entity(responseBody).build()
    }

    override fun getAccountDetails(accountId: String): Response {
        val account = accountService.getAccount(accountId)
        if (account !== null) {
            val responseBody = createAccountDetails(account)

            return Response.ok(responseBody).build()
        }
        return Response.status(Response.Status.NOT_FOUND).build()
    }

    private fun createAccountDetails(account: Account): AccountDetails {
        val responseBody = with(AccountDetails()) {
            id = account.id
            balance = BigDecimal.valueOf(account.getBalance().toLong()).movePointLeft(2)
            this
        }
        return responseBody
    }

}
package com.github.kpavlov.txservice.ws

import com.github.kpavlov.txservice.service.AccountService


class AccountsResource(private val accountService: AccountService) : AccountsApi() {

}
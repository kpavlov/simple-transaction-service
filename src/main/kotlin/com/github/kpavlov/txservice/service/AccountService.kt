package com.github.kpavlov.txservice.service

import com.github.kpavlov.txservice.domain.Account
import com.github.kpavlov.txservice.domain.AccountId

interface AccountService {

    fun createAccount(initialBalance: Int): Account

    fun getAccount(accountId: AccountId): Account?
}
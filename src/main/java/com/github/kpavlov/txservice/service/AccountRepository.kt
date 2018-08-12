package com.github.kpavlov.txservice.service

import com.github.kpavlov.txservice.domain.Account
import com.github.kpavlov.txservice.domain.AccountId

interface AccountRepository {

    fun createAccount(initialBalance: Int): AccountId

    fun getAccount(accountId: AccountId): Account?
}
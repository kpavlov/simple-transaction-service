package com.github.kpavlov.txservice.service

import com.github.kpavlov.txservice.domain.Account
import com.github.kpavlov.txservice.domain.AccountId
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class AccountServiceImpl : AccountService {

    private val accounts = ConcurrentHashMap<AccountId, Account>()

    override fun createAccount(initialBalance: Int): AccountId {
        val newId = UUID.randomUUID().toString()
        if (accounts.putIfAbsent(newId, Account(newId, initialBalance)) != null) {
            throw IllegalStateException("Account $newId already exists")
        }
        return newId
    }

    override fun getAccount(accountId: AccountId): Account? {
        return accounts[accountId]
    }

}
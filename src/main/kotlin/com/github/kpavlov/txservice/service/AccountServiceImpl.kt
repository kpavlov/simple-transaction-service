package com.github.kpavlov.txservice.service

import com.github.kpavlov.txservice.domain.Account
import com.github.kpavlov.txservice.domain.AccountId
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class AccountServiceImpl : AccountService {

    private val accounts = ConcurrentHashMap<AccountId, Account>()

    override fun createAccount(initialBalance: Int): Account {
        val newId = UUID.randomUUID().toString()
        val account = Account(newId, initialBalance)
        if (accounts.putIfAbsent(newId, account) != null) {
            throw IllegalStateException("Account $newId already exists")
        }
        return account
    }

    override fun getAccount(accountId: AccountId): Account? {
        return accounts[accountId]
    }

}
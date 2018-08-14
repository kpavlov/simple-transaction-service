package com.github.kpavlov.txservice.service

import com.github.kpavlov.txservice.domain.AccountId

interface TransactionService {

    fun transfer(amountCents: Int, debitAccountId: AccountId, creditAccountId: AccountId): TransactionResult
}
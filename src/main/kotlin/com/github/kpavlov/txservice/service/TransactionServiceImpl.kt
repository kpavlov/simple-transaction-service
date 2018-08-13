package com.github.kpavlov.txservice.service

import com.github.kpavlov.txservice.domain.AccountId

internal class TransactionServiceImpl(private val accountService: AccountService) : TransactionService {

    override fun transfer(amountCents: Int, fromAccountId: AccountId, toAccountId: AccountId): TransactionResult {
        val fromAccount = accountService.getAccount(fromAccountId)
                ?: return TransactionResult.DEBIT_ACCOUNT_NOT_FOUND
        val toAccount = accountService.getAccount(toAccountId)
                ?: return TransactionResult.CREDIT_ACCOUNT_NOT_FOUND

        return fromAccount.doWithLock { fromAcc ->

            if (fromAcc.getBalance() < amountCents) {
                return@doWithLock TransactionResult.NOT_ENOUGH_FUNDS
            }

            toAccount.doWithLock { toAcc ->
                fromAcc.amendBalance(-amountCents)
                toAcc.amendBalance(amountCents)
                TransactionResult.SUCCESS
            }
        }
    }


}
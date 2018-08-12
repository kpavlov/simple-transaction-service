package com.github.kpavlov.txservice.service

import com.github.kpavlov.txservice.domain.AccountId

internal class TransactionServiceImpl(private val accountRepository: AccountRepository) : TransactionService {

    override fun transfer(amountCents: Int, fromAccountId: AccountId, toAccountId: AccountId): TransactionResult {
        val fromAccount = accountRepository.getAccount(fromAccountId)
                ?: return TransactionResult.DEBIT_ACCOUNT_NOT_FOUND
        val toAccount = accountRepository.getAccount(toAccountId) ?: return TransactionResult.CREDIT_ACCOUNT_NOT_FOUND

        val result = fromAccount.doWithLock { fromAcc ->

            if (fromAcc.getBalance() < amountCents) {
                return@doWithLock TransactionResult.NOT_ENOUGH_FUNDS
            }

            toAccount.doWithLock { toAcc ->
                fromAcc.addFunds(-amountCents)
                toAcc.addFunds(amountCents)
                TransactionResult.SUCCESS
            }
        }

        return result
    }


}
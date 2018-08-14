package com.github.kpavlov.txservice.service

import com.github.kpavlov.txservice.domain.AccountId

internal class TransactionServiceImpl(private val accountService: AccountService) : TransactionService {

    override fun transfer(amountCents: Int, debitAccountId: AccountId, creditAccountId: AccountId): TransactionResult {
        val debitAccount = accountService.getAccount(debitAccountId)
                ?: return TransactionResult.DEBIT_ACCOUNT_NOT_FOUND
        val creditAccount = accountService.getAccount(creditAccountId)
                ?: return TransactionResult.CREDIT_ACCOUNT_NOT_FOUND

        val result = debitAccount.hold(amountCents)
        if (result == TransactionResult.SUCCESS) {
            creditAccount.addFunds(amountCents)
            debitAccount.withdrawHeldFunds(amountCents)
        }

        return result
    }


}
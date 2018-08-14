package com.github.kpavlov.txservice.domain

import com.github.kpavlov.txservice.service.TransactionResult
import java.util.concurrent.atomic.AtomicInteger

class Account(val id: AccountId, initialBalance: Int) {

    private val balanceHolder = AtomicInteger(initialBalance)
    private val holdAmountHolder = AtomicInteger()

    fun getBalance(): Int {
        return balanceHolder.get()
    }

    @Synchronized
    fun hold(delta: Int): TransactionResult {
        val currentBalance = balanceHolder.get()
        if (currentBalance < delta) {
            return TransactionResult.NOT_ENOUGH_FUNDS
        }
        holdAmountHolder.addAndGet(delta)
        balanceHolder.addAndGet(-delta)
        return TransactionResult.SUCCESS
    }

    /**
     * <b>EVENTUALLY</b> withdraw held amount
     */
    fun withdrawHeldFunds(amountCents: Int) {
        holdAmountHolder.addAndGet(-amountCents)
    }

    /**
     * <b>EVENTUALLY</b> add funds to balance
     */
    fun addFunds(delta: Int) {
        balanceHolder.addAndGet(delta)
    }
}
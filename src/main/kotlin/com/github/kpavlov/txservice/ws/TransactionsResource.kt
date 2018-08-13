package com.github.kpavlov.txservice.ws

import com.github.kpavlov.txservice.service.TransactionService
import com.github.kpavlov.txservice.ws.model.CreateTransactionRequest
import javax.ws.rs.core.Response

class TransactionsResource(private val transactionService: TransactionService) : TransactionsApi() {

    override fun createTransaction(request: CreateTransactionRequest): Response {

        val amountCents = request.amount.movePointRight(2).toInt()
        transactionService.transfer(amountCents, request.debitAccount, request.creditAccount)

        return Response.status(201).build()
    }
}
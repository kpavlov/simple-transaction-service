package com.github.kpavlov.txservice.ws

import com.github.kpavlov.txservice.service.TransactionResult.*
import com.github.kpavlov.txservice.service.TransactionService
import com.github.kpavlov.txservice.ws.model.CreateTransactionRequest
import com.github.kpavlov.txservice.ws.model.ErrorCode
import com.github.kpavlov.txservice.ws.model.ErrorResponse
import javax.ws.rs.core.Response

class TransactionsResource(private val transactionService: TransactionService) : TransactionsApi() {

    override fun createTransaction(request: CreateTransactionRequest): Response {

        val amountCents = request.amount.movePointRight(2).toInt()
        val result = transactionService.transfer(amountCents, request.debitAccount, request.creditAccount)

        return when (result) {
            SUCCESS -> Response.ok().build()
            INSUFFICIENT_FUNDS -> createResponse(422, ErrorCode.INSUFFICIENT_FUNDS)
            CREDIT_ACCOUNT_NOT_FOUND -> createResponse(404, ErrorCode.CREDIT_ACCOUNT_NOT_FOUND)
            DEBIT_ACCOUNT_NOT_FOUND -> createResponse(404, ErrorCode.DEBIT_ACCOUNT_NOT_FOUND)
        }
    }

    private fun createResponse(httpStatus: Int, errorCode: ErrorCode): Response {
        val body = with(ErrorResponse()) {
            status = httpStatus
            code = errorCode
            this
        }
        return Response.status(httpStatus).entity(body).build()
    }
}
package com.github.kpavlov.txservice.ws

import com.github.kpavlov.txservice.service.TransactionService

class TransactionsResource(private val transactionService: TransactionService) : TransactionsApi()
package com.github.kpavlov.txservice.service

enum class TransactionResult {

    SUCCESS,
    INSUFFICIENT_FUNDS,
    DEBIT_ACCOUNT_NOT_FOUND,
    CREDIT_ACCOUNT_NOT_FOUND
}
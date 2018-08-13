package com.github.kpavlov.txservice.service

enum class TransactionResult {

    SUCCESS,
    NOT_ENOUGH_FUNDS,
    DEBIT_ACCOUNT_NOT_FOUND,
    CREDIT_ACCOUNT_NOT_FOUND
}
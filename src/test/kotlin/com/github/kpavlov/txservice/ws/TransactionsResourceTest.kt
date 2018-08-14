package com.github.kpavlov.txservice.ws

import com.github.kpavlov.txservice.domain.AccountId
import com.github.kpavlov.txservice.service.TransactionResult
import com.github.kpavlov.txservice.service.TransactionService
import com.github.kpavlov.txservice.ws.model.CreateTransactionRequest
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.apache.commons.lang3.RandomUtils.nextInt
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*

internal class TransactionsResourceTest {

    private lateinit var transactionService: TransactionService
    private lateinit var subject: TransactionsResource

    private lateinit var request: CreateTransactionRequest
    private var amountCents = -1
    private lateinit var creditAccountId: AccountId
    private lateinit var debitAccountId: AccountId

    @BeforeEach
    fun setUp() {
        transactionService = mock()

        subject = TransactionsResource(transactionService)

        amountCents = nextInt(1, 100_00)
        creditAccountId = UUID.randomUUID().toString()
        debitAccountId = UUID.randomUUID().toString()
        request = CreateTransactionRequest()
                .amount(BigDecimal.valueOf(amountCents.toLong()).movePointLeft(2))
                .debitAccount(debitAccountId)
                .creditAccount(creditAccountId)
    }

    @Test
    fun shouldCreateTransaction() {
        // given
        whenever(transactionService.transfer(amountCents, debitAccountId, creditAccountId))
                .thenReturn(TransactionResult.SUCCESS)

        // when
        val response = subject.createTransaction(request)

        //then
        assertThat(response.status).`as`("httpStatus").isEqualTo(HttpStatus.SC_OK)
        assertThat(response.entity).`as`("entity").isNull()
        verify(transactionService).transfer(amountCents, debitAccountId, creditAccountId)
    }
}
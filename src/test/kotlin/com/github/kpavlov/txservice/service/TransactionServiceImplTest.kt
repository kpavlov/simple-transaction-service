package com.github.kpavlov.txservice.service

import com.github.kpavlov.txservice.domain.Account
import com.github.kpavlov.txservice.domain.AccountId
import com.github.kpavlov.txservice.domain.AccountMother
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.apache.commons.lang3.RandomUtils.nextInt
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TransactionServiceImplTest {

    private lateinit var subject: TransactionServiceImpl

    private lateinit var accountRepository: AccountService
    private lateinit var fromAccountId: AccountId
    private lateinit var fromAccount: Account
    private lateinit var toAccountId: AccountId
    private lateinit var toAccount: Account
    private var fromBalance = 0
    private var toBalance = 0

    @BeforeEach
    fun setUp() {
        accountRepository = mock()
        subject = TransactionServiceImpl(accountRepository)

        fromAccount = AccountMother.createRandomAccount()
        fromAccountId = fromAccount.id
        fromBalance = fromAccount.getBalance()

        toAccount = AccountMother.createRandomAccount()
        toAccountId = toAccount.id
        toBalance = toAccount.getBalance()

        whenever(accountRepository.getAccount(fromAccountId)).thenReturn(fromAccount)
        whenever(accountRepository.getAccount(toAccountId)).thenReturn(toAccount)
    }

    @Test
    fun shouldTransferMoney() {
        //given
        val amount = nextInt(10, fromBalance - 10)

        //when
        val result = subject.transfer(amount, fromAccountId, toAccountId)

        //then
        assertThat(result).isEqualTo(TransactionResult.SUCCESS)

        assertThat(fromAccount.getBalance()).isEqualTo(fromBalance - amount)
        assertThat(toAccount.getBalance()).isEqualTo(toBalance + amount)
    }

    @Test
    fun shouldNotTransferIfNotEnoughFunds() {
        //given
        val amount = fromBalance + 1

        //when
        val result = subject.transfer(amount, fromAccountId, toAccountId)

        //then
        assertThat(result).isEqualTo(TransactionResult.NOT_ENOUGH_FUNDS)

        assertThat(fromAccount.getBalance()).isEqualTo(fromBalance)
        assertThat(toAccount.getBalance()).isEqualTo(toBalance)
    }

    @Test
    fun shouldNotTransferFromUnknownAccount() {
        //given
        whenever(accountRepository.getAccount(fromAccountId)).thenReturn(null)
        val amount = fromBalance - 1

        //then
        val result = subject.transfer(amount, fromAccountId, toAccountId)

        //then
        assertThat(result).isEqualTo(TransactionResult.DEBIT_ACCOUNT_NOT_FOUND)
        assertThat(toAccount.getBalance()).isEqualTo(toBalance)
    }

    @Test
    fun shouldNotTransferToUnknownAccount() {
        //given
        whenever(accountRepository.getAccount(toAccountId)).thenReturn(null)
        val amount = fromBalance - 1

        //when
        val result = subject.transfer(amount, fromAccountId, toAccountId)

        //then
        assertThat(result).isEqualTo(TransactionResult.CREDIT_ACCOUNT_NOT_FOUND)
        assertThat(fromAccount.getBalance()).isEqualTo(fromBalance)
    }
}
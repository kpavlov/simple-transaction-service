package com.github.kpavlov.txservice.ws

import com.github.kpavlov.txservice.domain.Account
import com.github.kpavlov.txservice.domain.AccountMother
import com.github.kpavlov.txservice.service.AccountService
import com.github.kpavlov.txservice.ws.model.AccountDetails
import com.github.kpavlov.txservice.ws.model.CreateAccountRequest
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import org.apache.commons.lang3.RandomUtils
import org.apache.http.HttpStatus
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class AccountsResourceTest {

    private lateinit var accountService: AccountService
    private lateinit var subject: AccountsResource

    private lateinit var createRequest: CreateAccountRequest
    private var amountCents = -1
    private lateinit var account: Account

    @BeforeEach
    fun before() {
        accountService = mock()

        subject = AccountsResource(accountService)

        amountCents = RandomUtils.nextInt(1, 100_00)
        account = AccountMother.createRandomAccount(amountCents)

        createRequest = CreateAccountRequest()
                .initialBalance(BigDecimal.valueOf(amountCents.toLong()).movePointLeft(2))
    }

    @Test
    fun shouldCreateAccount() {
        //given
        whenever(accountService.createAccount(amountCents)).thenReturn(account)
        //when
        val response = subject.createAccount(createRequest)
        //then
        assertThat(response.status).`as`("httpStatus").isEqualTo(HttpStatus.SC_CREATED)
        assertThat(response.entity).`as`("entity").isInstanceOf(AccountDetails::class.java)
        val accountDetails = response.entity as AccountDetails
        assertThat(accountDetails.id).`as`("account.id").isEqualTo(account.id)
        assertThat(accountDetails.balance).`as`("account.balance").isEqualByComparingTo(createRequest.initialBalance)
    }
}
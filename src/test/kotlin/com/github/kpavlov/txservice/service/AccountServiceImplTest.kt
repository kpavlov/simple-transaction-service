package com.github.kpavlov.txservice.service

import com.github.kpavlov.txservice.domain.Account
import com.github.kpavlov.txservice.domain.AccountId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

internal class AccountServiceImplTest {

    private val subject = AccountServiceImpl()

    @Test
    fun shouldCreateAndGetAccount() {
        //given
        val initialBalance = Random().nextInt(10000) + 1

        //when
        val result = subject.createAccount(initialBalance)
        val accountId = result.id

        //then
        assertThat(accountId).isInstanceOf(AccountId::class.java)

        //when
        val account = subject.getAccount(accountId)
        assertThat(account).isInstanceOf(Account::class.java)
        account!!.let {
            assertThat(it.id).isEqualTo(accountId)
            assertThat(it.getBalance()).isEqualTo(initialBalance)
        }
    }

    @Test
    fun shouldNotGetNotExistingAccount() {
        //when
        val account = subject.getAccount(UUID.randomUUID().toString())

        //then
        assertThat(account).isNull()
    }
}
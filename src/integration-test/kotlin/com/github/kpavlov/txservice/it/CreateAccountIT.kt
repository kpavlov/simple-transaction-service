package com.github.kpavlov.txservice.it


import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.math.BigDecimal

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CreateAccountIT : AbstractIT() {

    @ParameterizedTest
    @ValueSource(longs = [0, 1, 100500])
    fun shouldCreateAccountWithBalance(balanceInt: Long) {

        val balance = BigDecimal.valueOf(balanceInt).movePointLeft(2)

        // when
        val accountId = TestClient.createAccount(balance)

        // then
        assertThat(accountId).`as`("AccountId").isNotBlank()

        // and then
        val accountDetails = TestClient.getAccountDetails(accountId)

        assertThat(accountId).`as`("accountDetails").isInstanceOf(AccountDetails::class.java)

        with(accountDetails) {
            assertThat(id).`as`("account.id").isEqualTo(accountId)
            assertThat(balance).`as`("account.balance").isEqualByComparingTo(balance)
        }
    }
}
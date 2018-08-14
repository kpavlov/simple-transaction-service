package com.github.kpavlov.txservice.it


import com.github.kpavlov.txservice.domain.AccountId
import org.apache.commons.lang3.RandomUtils.nextInt
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.math.BigDecimal

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransferMoneyIT : AbstractIT() {

    private lateinit var accountId1: AccountId
    private lateinit var accountId2: AccountId
    private var balance1Cents = -1
    private var balance2Cents = -1

    @BeforeEach
    fun before() {
        balance1Cents = nextInt(1, 100500)
        balance2Cents = nextInt(1, 100500)
        accountId1 = TestClient.createAccount(BigDecimal(balance1Cents).movePointLeft(2))
        accountId2 = TestClient.createAccount(BigDecimal(balance2Cents).movePointLeft(2))
    }

    @Test
    fun shouldTransferMoney() {

        val transferAmountCents = nextInt(1, balance1Cents)

        // when

        TestClient.createTransaction(BigDecimal(transferAmountCents).movePointLeft(2), accountId1, accountId2)

        // then
        val balance1AfterTransfer = TestClient.getAccountDetails(accountId1).balance.movePointRight(2).toInt()
        val balance2AfterTransfer = TestClient.getAccountDetails(accountId2).balance.movePointRight(2).toInt()

        assertThat(balance1AfterTransfer).isEqualTo(balance1Cents - transferAmountCents)
        assertThat(balance2AfterTransfer).isEqualTo(balance2Cents + transferAmountCents)
    }
}
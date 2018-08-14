package com.github.kpavlov.txservice.service

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.awaitAll
import kotlinx.coroutines.experimental.runBlocking
import org.apache.commons.lang3.RandomUtils.nextInt
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTimeoutPreemptively
import org.junit.jupiter.api.Test
import java.time.Duration.ofSeconds

private const val MAX_COROUTINES = 100500
private const val TIMEOUT_SECONDS = 15L

class TransactionServiceConcurrencyTest {

    private val accountService = AccountServiceImpl()
    private val subject = TransactionServiceImpl(accountService)

    private val aggregateBalance = nextInt(100500, 200600)

    private val accounts = arrayOf(
            accountService.createAccount(aggregateBalance),
            accountService.createAccount(0)
    )

    @Test
    fun shouldRunConcurrently() {
        //when
        val deffer = (1..MAX_COROUTINES).map { n ->
            async {
                val debitAccount = accounts[n % 2]
                val creditAccount = accounts[(n + 1) % 2]
                val amountCents = nextInt(0, debitAccount.getBalance())
                if (amountCents == 0) {
                    return@async 1
                }
                //println("[coroutine#$n] Transfer $amountCents¢ ${debitAccount.id} -> ${creditAccount.id}")
                subject.transfer(amountCents, debitAccount.id, creditAccount.id)
                1
            }
        }

        assertTimeoutPreemptively(ofSeconds(TIMEOUT_SECONDS),
                { runBlocking { deffer.awaitAll() } },
                "Deadlock detected")
        // I don't care about cancelling running coroutines in test (because JVM will be killed eventually)

        //then
        val finalAggregateBalance = accounts.sumBy { it.getBalance() }

        assertThat(finalAggregateBalance)
                .`as`("Law of conservation of quantity of money is violated")
                .isEqualTo(aggregateBalance)
    }
}
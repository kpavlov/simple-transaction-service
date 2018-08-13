package com.github.kpavlov.txservice.domain

import org.apache.commons.lang3.RandomUtils.nextInt
import java.util.*

object AccountMother {

    fun createRandomAccount(amountCents: Int = nextInt(1_00, 1000_00)): Account {
        val id = UUID.randomUUID().toString()
        return Account(id, amountCents)
    }
}
package com.github.kpavlov.txservice.domain

import org.apache.commons.lang3.RandomUtils.nextInt
import java.util.*

object AccountMother {

    fun createRandomAccount() : Account {
        val id = UUID.randomUUID().toString()
        val balance = nextInt(1_00, 1000_00)
        return Account(id, balance)
    }
}
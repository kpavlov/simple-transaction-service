package com.github.kpavlov.txservice.domain

import java.util.*

object AccountMother {

    fun createRandomAccount() : Account {
        val id = UUID.randomUUID().toString()
        val balance = 1_00 + Random().nextInt(1000_00)
        return Account(id, balance)
    }
}
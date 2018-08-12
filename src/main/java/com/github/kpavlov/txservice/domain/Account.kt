package com.github.kpavlov.txservice.domain

import java.util.concurrent.locks.ReentrantReadWriteLock

class Account(val id: AccountId, initialBalance: Int) {

    private var balance = initialBalance

    private val lock = ReentrantReadWriteLock()

    fun getBalance(): Int {
        try {
            lock.readLock().lock()
            return balance
        } finally {
            lock.readLock().unlock()
        }
    }

    fun <T> doWithLock(action: (Account) -> T): T {
        try {
            lock.writeLock().lock()
            return action(this)
        } finally {
            lock.writeLock().unlock()
        }
    }

    fun addFunds(delta: Int) {
        balance += delta
    }
}
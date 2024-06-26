package com.example.data.base

import com.example.data.di.dataTestModule
import org.junit.Before
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.AutoCloseKoinTest


abstract class BaseDataTest: AutoCloseKoinTest() {

    @Before
    fun before() {
        stopKoin()
        startKoin {
            modules(
                dataTestModule
            )
        }
    }
}
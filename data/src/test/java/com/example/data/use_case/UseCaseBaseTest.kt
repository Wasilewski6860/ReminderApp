package com.example.data.use_case

import com.example.data.di.useCaseModule
import com.example.domain.repository.IGroupRepository
import com.example.domain.repository.ITaskRepository
import org.junit.Before
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject

open class UseCaseBaseTest : AutoCloseKoinTest() {

    val taskRepository: ITaskRepository by inject()
    val groupRepository: IGroupRepository by inject()

    @Before
    fun before() {
        stopKoin()
        startKoin {
            modules(useCaseModule)
        }
    }

}
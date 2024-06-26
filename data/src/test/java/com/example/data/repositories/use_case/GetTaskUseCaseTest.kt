package com.example.data.repositories.use_case

import com.example.data.repositories.MockTaskRepositoryImpl
import com.example.domain.repository.TaskRepository
import com.example.domain.use_case.GetTaskUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Before
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module._singleInstanceFactory
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject

class GetTaskUseCaseTest : AutoCloseKoinTest() {

    @Before
    fun before() {
        stopKoin()
        startKoin {
            modules(
                module { single { MockTaskRepositoryImpl() } }
            )
        }
    }

    private val repository: TaskRepository by inject()
    private val useCase = GetTaskUseCase(repository)

    @Test
    fun `should return same data as repository`() = runTest {
        val expected = repository.getTask(id = 0).first()
        val actual = useCase(id = 0).first()
        assertEquals(
            expected,
            actual
        )
    }

}
package com.example.data.repository

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.TestData
import com.example.data.base.BaseDataTest
import com.example.data.cache.TaskStorage
import com.example.data.di.dataTestModule
import com.example.data.reminder.RemindAlarmManager
import com.example.data.repositories.TaskRepositoryImpl
import com.example.domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.inject
import androidx.test.platform.app.InstrumentationRegistry

class TaskRepositoryTest: AutoCloseKoinTest() {

    val context: Context = mockk<Context>()
    private val taskStorage: TaskStorage = mockk()
    private val repository: TaskRepository by inject()

    @Before
    fun before() {
        stopKoin()
        startKoin {
            modules(
                module {
                    single<TaskStorage> { mockk() }
                    single<TaskRepository> { TaskRepositoryImpl(get(), context, mockk()) }
                }
            )
        }
    }

    @Test
    fun `test addTask`() = runBlocking {
        // Mock Task object
        val task = TestData.firstTask

        // Mock behavior of taskStorage.addTask
        every { taskStorage.addTask(task) } returns flow { emit(task) }

        // Collect the result from repository.addTask
        repository.addTask(task).collect { result ->
            assert(result == task)
        }

        // Verify that taskStorage.addTask was called with the correct task
        verify { taskStorage.addTask(task) }
    }

}
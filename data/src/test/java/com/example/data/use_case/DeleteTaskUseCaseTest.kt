package com.example.data.use_case

import com.example.data.TestData
import com.example.domain.use_case.task.DeleteTaskUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.component.inject

class DeleteTaskUseCaseTest : UseCaseBaseTest() {

    private val useCase: DeleteTaskUseCase by inject()

    @Test
    fun `deleting task by id process test`() = runTest {
        coEvery { taskRepository.deleteTask(id = any()) } returns Unit
        useCase(id = 0)
        coVerify { taskRepository.deleteTask(id = 0) }
    }

    @Test
    fun `deleting task by data process test`() = runTest {
        coEvery { taskRepository.deleteTask(task = any()) } returns Unit
        useCase(task = TestData.firstTask)
        coVerify { taskRepository.deleteTask(TestData.firstTask) }
    }

}
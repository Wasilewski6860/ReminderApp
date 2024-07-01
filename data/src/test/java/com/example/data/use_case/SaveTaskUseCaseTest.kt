package com.example.data.use_case

import com.example.data.TestData
import com.example.domain.use_case.task.SaveTaskUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.component.inject

class SaveTaskUseCaseTest : UseCaseBaseTest() {

    private val useCase: SaveTaskUseCase by inject()

    @Test
    fun `saving task process test`() = runTest {
        coEvery { taskRepository.addTask(task = any()) } returns flowOf(TestData.firstTask)
        useCase(task = TestData.firstTask)
        coVerify { taskRepository.addTask(task = TestData.firstTask) }
    }

}
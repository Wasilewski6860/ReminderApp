package com.example.data.use_case

import com.example.data.TestData
import com.example.domain.use_case.task.EditTaskUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.component.inject

class EditTaskUseCaseTest : UseCaseBaseTest() {

    private val useCase: EditTaskUseCase by inject()

    @Test
    fun `editing task process test`() = runTest {
        coEvery { taskRepository.editTask(task = any()) } returns Unit
        useCase(task = TestData.firstTask)
        coVerify { taskRepository.editTask(task = TestData.firstTask) }
    }

}
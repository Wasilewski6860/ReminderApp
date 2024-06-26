package com.example.data.use_case

import com.example.domain.use_case.task.ClearAllTasksUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.component.inject

class ClearAllTasksUseCaseTest : UseCaseBaseTest() {

    private val useCase: ClearAllTasksUseCase by inject()

    @Test
    fun `deleting all tasks process test`() = runTest {
        coEvery { taskRepository.deleteAll() } returns Unit
        useCase()
        coVerify { taskRepository.deleteAll() }
    }

}
package com.example.data.use_case

import com.example.domain.use_case.ClearAllTasksUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.component.inject

class ClearAllTasksUseCaseTest : UseCaseBaseTest() {

    private val useCase: ClearAllTasksUseCase by inject()

    @Test
    fun `deleting all tasks process test`() = runTest {
        coEvery { repository.deleteAll() } returns Unit
        useCase(Unit)
        coVerify { repository.deleteAll() }
    }

}
package com.example.data.use_case

import com.example.data.TestData
import com.example.domain.use_case.task.GetTasksWithFlagCountUseCase
import io.mockk.coEvery
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.component.inject
import kotlin.test.assertEquals

class GetTasksWithFlagCountUseCaseTest : UseCaseBaseTest() {

    private val useCase: GetTasksWithFlagCountUseCase by inject()

    @Test
    fun `getting tasks with flag count process test`() = runTest {
        coEvery { taskRepository.getTasksWithFlagCount() } returns flowOf(TestData.tasksWithFlag.size)
        val expected = TestData.tasksWithFlag.size
        val actual = useCase().first()
        assertEquals(expected, actual)
    }

}
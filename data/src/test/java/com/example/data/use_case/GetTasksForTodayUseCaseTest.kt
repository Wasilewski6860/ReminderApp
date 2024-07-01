package com.example.data.use_case

import com.example.data.TestData
import com.example.domain.use_case.task.GetTasksForTodayUseCase
import io.mockk.coEvery
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.component.inject
import kotlin.test.assertEquals

class GetTasksForTodayUseCaseTest : UseCaseBaseTest() {

    private val useCase: GetTasksForTodayUseCase by inject()

    @Test
    fun `getting today tasks process test`() = runTest {
        coEvery { taskRepository.getTasksForToday() } returns flowOf(TestData.todayTasks)
        val expected = TestData.todayTasks
        val actual = useCase().first()
        assertEquals(expected, actual)
    }

}
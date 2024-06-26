package com.example.data.use_case

import com.example.data.TestData
import com.example.domain.use_case.GetTasksForTodayCountUseCase
import io.mockk.coEvery
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.component.inject
import kotlin.test.assertEquals

class GetTasksForTodayCountUseCaseTest : UseCaseBaseTest() {

    private val useCase: GetTasksForTodayCountUseCase by inject()

    @Test
    fun `getting tasks for today count process`() = runTest {
        coEvery { repository.getTasksForTodayCount() } returns flowOf(TestData.todayTasks.size)
        val expected = TestData.todayTasks.size
        val actual = useCase(Unit).first()
        assertEquals(expected, actual)
    }

}
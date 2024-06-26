package com.example.data.use_case

import com.example.data.TestData
import com.example.domain.use_case.GetTasksPlannedCountUseCase
import io.mockk.coEvery
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.component.inject
import kotlin.test.assertEquals

class GetTasksPlannedCountUseCaseTest : UseCaseBaseTest() {

    private val useCase: GetTasksPlannedCountUseCase by inject()

    @Test
    fun `getting planned tasks count process test`() = runTest {
        coEvery { repository.getTasksPlannedCount() } returns flowOf(TestData.plannedTasks.size)
        val expected = TestData.plannedTasks.size
        val actual = useCase(Unit).first()
        assertEquals(expected, actual)
    }

}
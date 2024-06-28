package com.example.data.use_case

import com.example.data.TestData
import com.example.domain.use_case.task.GetPlannedTasksUseCase
import io.mockk.coEvery
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.component.inject
import kotlin.test.assertEquals

class GetPlannedTasksUseCaseTest : UseCaseBaseTest() {

    private val useCase: GetPlannedTasksUseCase by inject()

    @Test
    fun `getting planned tasks process test`() = runTest {
        coEvery { repository.getTasksPlanned() } returns flowOf(TestData.plannedTasks)
        val expected = TestData.plannedTasks
        val actual = useCase(Unit).first()
        assertEquals(expected, actual)
    }

}
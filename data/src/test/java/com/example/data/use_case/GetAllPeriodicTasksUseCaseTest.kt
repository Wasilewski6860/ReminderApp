package com.example.data.use_case

import com.example.data.TestData
import com.example.domain.model.TaskPeriodType
import com.example.domain.use_case.task.GetAllPeriodicTasksUseCase
import io.mockk.coEvery
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.component.inject
import kotlin.test.assertEquals

class GetAllPeriodicTasksUseCaseTest : UseCaseBaseTest() {

    private val useCase: GetAllPeriodicTasksUseCase by inject()

    @Test
    fun `getting all periodic tasks process test`() = runTest {
        coEvery { repository.getAllTasksByPeriodType(period = TaskPeriodType.PERIODIC) } returns flowOf(
            TestData.periodicTimeTasks
        )
        val expected = TestData.periodicTimeTasks
        val actual = useCase(Unit).first()
        assertEquals(expected, actual)
    }

}
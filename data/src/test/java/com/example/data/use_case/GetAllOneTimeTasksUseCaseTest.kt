package com.example.data.use_case

import com.example.data.TestData
import com.example.domain.model.TaskPeriodType
import com.example.domain.use_case.GetAllOneTimeTasksUseCase
import io.mockk.coEvery
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.component.inject
import kotlin.test.assertEquals

class GetAllOneTimeTasksUseCaseTest : UseCaseBaseTest() {

    private val useCase: GetAllOneTimeTasksUseCase by inject()

    @Test
    fun `getting all one time tasks process test`() = runTest {
        coEvery { repository.getAllTasksByPeriodType(period = TaskPeriodType.ONE_TIME) } returns flowOf(
            TestData.noTimeTasks
        )
        val expected = TestData.noTimeTasks
        val actual = useCase(Unit).first()
        assertEquals(expected, actual)
    }

}
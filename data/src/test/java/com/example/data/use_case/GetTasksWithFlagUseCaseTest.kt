package com.example.data.use_case

import com.example.data.TestData
import com.example.domain.use_case.task.GetTasksWithFlagUseCase
import io.mockk.coEvery
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.component.inject
import kotlin.test.assertEquals

class GetTasksWithFlagUseCaseTest : UseCaseBaseTest() {

    private val useCase: GetTasksWithFlagUseCase by inject()

    @Test
    fun `getting tasks with flag process test`() = runTest {
        coEvery { repository.getTasksWithFlag() } returns flowOf(TestData.tasksWithFlag)
        val expected = TestData.tasksWithFlag
        val actual = useCase(Unit).first()
        assertEquals(expected, actual)
    }

}
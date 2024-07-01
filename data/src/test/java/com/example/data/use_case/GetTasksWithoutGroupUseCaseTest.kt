package com.example.data.use_case

import com.example.data.TestData
import com.example.domain.use_case.task.GetTasksWithoutGroupUseCase
import io.mockk.coEvery
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.component.inject
import kotlin.test.assertEquals

class GetTasksWithoutGroupUseCaseTest : UseCaseBaseTest() {

    private val useCase: GetTasksWithoutGroupUseCase by inject()

    @Test
    fun `get tasks without group process test`() = runTest {
        coEvery { taskRepository.getTasksWithoutGroup() } returns flowOf(TestData.tasksWithoutGroup)
        val expected = TestData.tasksWithoutGroup
        val actual = useCase().first()
        assertEquals(expected, actual)
    }

}
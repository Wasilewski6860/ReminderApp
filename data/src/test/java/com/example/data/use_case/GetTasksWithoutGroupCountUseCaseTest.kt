package com.example.data.use_case

import com.example.data.TestData
import com.example.domain.use_case.task.GetTasksWithoutGroupCountUseCase
import io.mockk.coEvery
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.test.inject
import kotlin.test.assertEquals

class GetTasksWithoutGroupCountUseCaseTest : UseCaseBaseTest() {

    private val useCase: GetTasksWithoutGroupCountUseCase by inject()

    @Test
    fun `getting tasks without group count process test`() = runTest {
        coEvery { taskRepository.getTasksWithoutGroupCount() } returns flowOf(TestData.tasksWithoutGroup.size)
        val expected = TestData.tasksWithoutGroup.size
        val actual = useCase().first()
        assertEquals(expected, actual)
    }

}
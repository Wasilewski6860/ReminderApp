package com.example.data.use_case

import com.example.data.TestData
import com.example.domain.use_case.task.GetAllTasksUseCase
import io.mockk.coEvery
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.component.inject
import kotlin.test.assertEquals

class GetAllTasksUseCaseTest : UseCaseBaseTest() {

    private val useCase: GetAllTasksUseCase by inject()

    @Test
    fun `getting all tasks process test`() = runTest {
        coEvery { repository.getAllTasks() } returns flowOf(TestData.tasks)
        val expected = TestData.tasks
        val actual = useCase(Unit).first()
        assertEquals(expected, actual)
    }

}
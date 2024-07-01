package com.example.data.use_case

import com.example.data.TestData
import com.example.domain.use_case.task.GetTaskUseCase
import io.mockk.coEvery
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.assertEquals
import org.koin.test.inject

class GetTaskUseCaseTest : UseCaseBaseTest() {

    private val useCase: GetTaskUseCase by inject()

    @Test
    fun `getting task process test`() = runTest {
        coEvery { taskRepository.getTask(any()) } returns flowOf(TestData.firstTask)
        val expected = TestData.firstTask
        val actual = useCase(id = 0).first()
        assertEquals(expected, actual)
    }

}
package com.example.data.use_case

import com.example.data.TestData
import com.example.domain.use_case.task.GetNoTimeTasksCountUseCase
import io.mockk.coEvery
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.component.inject
import kotlin.test.assertEquals

class GetNoTimeTasksCountUseCaseTest : UseCaseBaseTest() {

    private val useCase: GetNoTimeTasksCountUseCase by inject()

    @Test
    fun `getting tasks with no time count process`() = runTest {
        coEvery { taskRepository.getCountOfNoTimeTasks() } returns flowOf(TestData.noTimeTasks.size)
        val expected = TestData.noTimeTasks.size
        val actual = useCase().first()
        assertEquals(expected, actual)
    }

}
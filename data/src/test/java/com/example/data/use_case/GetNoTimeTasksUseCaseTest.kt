package com.example.data.use_case

import com.example.data.TestData
import com.example.domain.use_case.task.GetNoTimeTasksUseCase
import io.mockk.coEvery
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.component.inject
import kotlin.test.assertEquals

class GetNoTimeTasksUseCaseTest : UseCaseBaseTest() {

    private val useCase: GetNoTimeTasksUseCase by inject()

    @Test
    fun `getting tasks with no time process test`() = runTest {
        coEvery { repository.getNoTimeTasks() } returns flowOf(TestData.noTimeTasks)
        val expected = TestData.noTimeTasks
        val actual = useCase(Unit).first()
        assertEquals(expected, actual)
    }

}
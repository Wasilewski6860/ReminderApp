package com.example.data.use_case

import com.example.data.TestData
import com.example.domain.use_case.GetAllTasksCountUseCase
import io.mockk.coEvery
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.component.inject
import kotlin.test.assertEquals

class GetAllTasksCountUseCaseTest : UseCaseBaseTest() {

    private val useCase: GetAllTasksCountUseCase by inject()

    @Test
    fun `getting all tasks count process test`() = runTest {
        coEvery { repository.getAllTasksCount() } returns flowOf(TestData.tasks.size)
        val expected = TestData.tasks.size
        val actual = useCase(Unit).first()
        assertEquals(expected, actual)
    }

}
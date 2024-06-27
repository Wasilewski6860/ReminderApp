package com.example.data.use_case

import com.example.data.TestData
import com.example.domain.use_case.group.GetGroupWithTasksUseCase
import io.mockk.coEvery
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.component.inject
import kotlin.test.assertEquals

class GetGroupWithTasksUseCaseTest : UseCaseBaseTest() {

    private val useCase: GetGroupWithTasksUseCase by inject()

    @Test
    fun `getting group with tasks process test`() = runTest {
        coEvery { repository.getGroupWithTasks(id = any()) } returns flowOf(TestData.groupWithTasks)
        val expected = TestData.groupWithTasks
        val actual = useCase(TestData.firstGroup.groupId).first()
        assertEquals(expected, actual)
    }

}
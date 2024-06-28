package com.example.data.use_case

import com.example.data.TestData
import com.example.domain.use_case.group.GetGroupUseCase
import io.mockk.coEvery
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.component.inject
import kotlin.test.assertEquals

class GetGroupUseCaseTest : UseCaseBaseTest() {

    private val useCase: GetGroupUseCase by inject()

    @Test
    fun `getting group process test`() = runTest {
        coEvery { groupRepository.getGroup(id = any()) } returns flowOf(TestData.firstGroup)
        val expected = TestData.firstGroup
        val actual = useCase(TestData.firstGroup.groupId).first()
        assertEquals(expected, actual)
    }

}
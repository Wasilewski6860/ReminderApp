package com.example.data.use_case

import com.example.data.TestData
import com.example.domain.use_case.group.GetAllGroupsUseCase
import io.mockk.coEvery
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.component.inject
import kotlin.test.assertEquals

class GetAllGroupsUseCaseTest : UseCaseBaseTest() {

    private val useCase: GetAllGroupsUseCase by inject()

    @Test
    fun `getting all groups process test`() = runTest {
        coEvery { groupRepository.getAllGroups() } returns flowOf(TestData.groups)
        val expected = TestData.groups
        val actual = useCase().first()
        assertEquals(expected, actual)
    }

}
package com.example.data.use_case

import com.example.data.TestData
import com.example.domain.use_case.DeleteGroupUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.component.inject

class DeleteGroupUseCaseTest : UseCaseBaseTest() {

    private val useCase: DeleteGroupUseCase by inject()

    @Test
    fun `group deleting process test`() = runTest {
        coEvery { repository.deleteGroup(groupId = any()) } returns Unit
        useCase(groupId = TestData.firstGroup.groupId)
        coVerify { repository.deleteGroup(groupId = TestData.firstGroup.groupId) }
    }

}
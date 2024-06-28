package com.example.data.use_case

import com.example.data.TestData
import com.example.domain.use_case.group.EditGroupUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.component.inject

class EditGroupUseCaseTest : UseCaseBaseTest() {

    private val useCase: EditGroupUseCase by inject()

    @Test
    fun `editing group process test`() = runTest {
        coEvery { repository.editGroup(group = any()) } returns Unit
        useCase(group = TestData.firstGroup)
        coVerify { repository.editGroup(group = TestData.firstGroup) }
    }

}
package com.example.data.use_case

import com.example.data.TestData
import com.example.domain.use_case.group.InsertGroupUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.component.inject

class InsertGroupUseCaseTest : UseCaseBaseTest() {

    private val useCase: InsertGroupUseCase by inject()

    @Test
    fun `inserting group process test`() = runTest {
        coEvery { repository.addGroup(group = any()) } returns 1L
        useCase(group = TestData.firstGroup)
        coVerify { repository.addGroup(group = TestData.firstGroup) }
    }

}
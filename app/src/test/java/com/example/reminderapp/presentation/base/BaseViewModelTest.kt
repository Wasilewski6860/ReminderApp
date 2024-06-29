package com.example.reminderapp.presentation.base

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.example.reminderapp.di.presentationTestModule
import com.example.reminderapp.di.dataTestModule
import com.example.reminderapp.di.domainModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(
    sdk = [26, 27, 28, 29, 30, 31, 32],
    manifest = Config.NONE
)
abstract class BaseViewModelTest: AutoCloseKoinTest() {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun before() {
        stopKoin()
        startKoin {
            modules(
                module {
                    single<Application> {
                        ApplicationProvider.getApplicationContext()
                    }
                },
                presentationTestModule, domainModule, dataTestModule,
            )
        }
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
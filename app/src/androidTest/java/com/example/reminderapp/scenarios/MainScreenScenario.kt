package com.example.reminderapp.scenarios

import android.os.Bundle
import com.example.reminderapp.R
import com.example.reminderapp.presentation.mainscreen.MainFragment
import com.kaspersky.kaspresso.testcases.api.scenario.Scenario
import com.kaspersky.kaspresso.testcases.core.testcontext.TestContext

//class LoginScenario : Scenario() {
//
//    override val steps: TestContext<Unit>.() -> Unit = {
//        step("Open login screen") {
//            activityTestRule.launchActivity(null)
//            activityTestRule.activity.runOnUiThread {
//                val fragment = MainFragment().apply {
//                    arguments = Bundle().apply {
//                        // Добавьте аргументы для фрагмента, если необходимо
//                    }
//                }
//                activityTestRule.activity.supportFragmentManager
//                    .beginTransaction()
//                    .replace(R.id.fragmentContainerView, fragment)
//                    .commitNow()
//            }
//        }
//        step("Check elements visibility") {
//            LoginScreen {
//                inputUsername {
//                    isVisible()
//                    hasHint(R.string.login_activity_hint_username)
//                }
//                inputPassword {
//                    isVisible()
//                    hasHint(R.string.login_activity_hint_password)
//                }
//                loginButton {
//                    isVisible()
//                    isClickable()
//                }
//            }
//        }
//        step("Try to login") {
//            LoginScreen {
//                inputUsername {
//                    replaceText(username)
//                }
//                inputPassword {
//                    replaceText(password)
//                }
//                loginButton {
//                    click()
//                }
//            }
//        }
//    }
//}

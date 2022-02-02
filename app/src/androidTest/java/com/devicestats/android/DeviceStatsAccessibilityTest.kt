package com.devicestats.android

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test

class DeviceStatsAccessibilityTest {

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java, true, true)

    @Test
    fun testAccessibility() {
        Espresso.onView(withId(R.id.viewpager)).perform(ViewActions.click())
    }
}

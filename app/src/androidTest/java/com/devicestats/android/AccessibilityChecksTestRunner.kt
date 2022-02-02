package com.devicestats.android

import androidx.test.espresso.accessibility.AccessibilityChecks
import androidx.test.runner.AndroidJUnitRunner

class AccessibilityChecksTestRunner : AndroidJUnitRunner() {

    init {
        AccessibilityChecks.enable().setRunChecksFromRootView(true)
    }
}

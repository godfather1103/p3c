package com.alibaba.p3c.idea.listener

import com.alibaba.p3c.idea.util.HighlightInfoTypes
import com.alibaba.p3c.idea.util.HighlightSeverities
import com.intellij.codeInsight.daemon.impl.SeverityRegistrar
import com.intellij.ide.AppLifecycleListener

class CommonSettingsApplicationListener: AppLifecycleListener {
    /**
     * Called after all application startup tasks, including opening projects, are processed (i.e. either completed or running in background).
     */
    override fun appStarted() {
        SeverityRegistrar.registerStandard(HighlightInfoTypes.BLOCKER, HighlightSeverities.BLOCKER)
        SeverityRegistrar.registerStandard(HighlightInfoTypes.CRITICAL, HighlightSeverities.CRITICAL)
        SeverityRegistrar.registerStandard(HighlightInfoTypes.MAJOR, HighlightSeverities.MAJOR)
    }
}
package com.alibaba.p3c.idea.listener

import com.alibaba.p3c.idea.compatible.inspection.Inspections
import com.alibaba.p3c.idea.config.P3cConfig
import com.alibaba.p3c.idea.i18n.P3cBundle
import com.alibaba.p3c.idea.service.FileListenerService
import com.alibaba.p3c.idea.util.HighlightInfoTypes
import com.alibaba.p3c.idea.util.HighlightSeverities
import com.alibaba.p3c.pmd.I18nResources
import com.alibaba.p3c.pmd.lang.java.util.namelist.NameListConfig
import com.alibaba.smartfox.idea.common.util.getService
import com.flag.Version
import com.intellij.codeInsight.daemon.impl.SeverityRegistrar
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import com.intellij.openapi.startup.ProjectActivity
import io.github.godfather1103.service.BaseNameListServiceExt

class MyProjectStartupActivity : ProjectActivity, ProjectManagerListener {

    private val p3cConfig = P3cConfig::class.java.getService()

    override suspend fun execute(project: Project) {
        registerStandard()
        println("this is ${Version.version()}")
        I18nResources.changeLanguage(p3cConfig.locale)
        val service = NameListConfig.NAME_LIST_SERVICE
        if (service is BaseNameListServiceExt) {
            val inData = p3cConfig.customNamelistProperties
            if (service.oldData != inData) {
                service.resetData(inData)
            }
        }
        val analyticsGroup = ActionManager.getInstance().getAction(analyticsGroupId)
        analyticsGroup.templatePresentation.text = P3cBundle.getMessage(analyticsGroupText)
        Inspections.addCustomTag(project, "date")
        val fileService = project.getService(FileListenerService::class.java)
        fileService.projectOpened(project)
    }

    private fun registerStandard() {
        if (!SeverityRegistrar.isDefaultSeverity(HighlightSeverities.BLOCKER)) {
            SeverityRegistrar.registerStandard(HighlightInfoTypes.BLOCKER, HighlightSeverities.BLOCKER)
        }
        if (!SeverityRegistrar.isDefaultSeverity(HighlightSeverities.CRITICAL)) {
            SeverityRegistrar.registerStandard(HighlightInfoTypes.CRITICAL, HighlightSeverities.CRITICAL)
        }
        if (!SeverityRegistrar.isDefaultSeverity(HighlightSeverities.MAJOR)) {
            SeverityRegistrar.registerStandard(HighlightInfoTypes.MAJOR, HighlightSeverities.MAJOR)
        }
    }

    override fun projectClosed(project: Project) {
        val fileService = project.getService(FileListenerService::class.java)
        fileService.projectClosed()
    }

    companion object {
        const val analyticsGroupId = "com.alibaba.p3c.analytics.action_group"
        const val analyticsGroupText = "$analyticsGroupId.text"
    }

}

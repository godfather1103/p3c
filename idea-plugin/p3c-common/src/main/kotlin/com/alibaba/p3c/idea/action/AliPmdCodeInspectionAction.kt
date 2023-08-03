package com.alibaba.p3c.idea.action

import com.alibaba.p3c.idea.compatible.inspection.InspectionProfileService
import com.alibaba.p3c.idea.compatible.inspection.Inspections
import com.alibaba.p3c.idea.i18n.P3cBundle
import com.alibaba.p3c.idea.inspection.AliBaseInspection
import com.alibaba.p3c.idea.util.NumberConstants
import com.google.common.collect.Lists
import com.intellij.analysis.AnalysisScope
import com.intellij.analysis.AnalysisUIOptions
import com.intellij.analysis.BaseAnalysisActionDialog
import com.intellij.codeInspection.InspectionManager
import com.intellij.codeInspection.actions.CodeInspectionAction
import com.intellij.codeInspection.ex.InspectionManagerEx
import com.intellij.codeInspection.ex.InspectionToolWrapper
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.module.Module
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileSystemItem
import com.intellij.psi.PsiManager
import java.awt.event.KeyEvent
import java.util.*

class AliPmdCodeInspectionAction : CodeInspectionAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val analysisUIOptions = project.getService(AnalysisUIOptions::class.java)!!
        analysisUIOptions.GROUP_BY_SEVERITY = true

        val managerEx = InspectionManager.getInstance(project) as InspectionManagerEx
        val toolWrappers = Inspections.aliInspections(project) {
            it.tool is AliBaseInspection
        }
        val psiElement = e.getData(CommonDataKeys.PSI_ELEMENT)
        val psiFile = e.getData(CommonDataKeys.PSI_FILE)
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE)
        val virtualFiles = e.getData<Array<VirtualFile>>(CommonDataKeys.VIRTUAL_FILE_ARRAY)
        var analysisScope: AnalysisScope? = null
        var projectDir = false
        if (psiFile != null) {
            analysisScope = AnalysisScope(psiFile)
            projectDir = isBaseDir(psiFile.virtualFile, project)
        } else if (virtualFiles != null && virtualFiles.size > NumberConstants.INTEGER_SIZE_OR_LENGTH_0) {
            analysisScope = AnalysisScope(project, Lists.newArrayList<VirtualFile>(*virtualFiles))
            projectDir = virtualFiles.any {
                isBaseDir(it, project)
            }
        } else {
            if (virtualFile != null && virtualFile.isDirectory) {
                val psiDirectory = PsiManager.getInstance(project).findDirectory(virtualFile)
                if (psiDirectory != null) {
                    analysisScope = AnalysisScope(psiDirectory)
                    projectDir = isBaseDir(virtualFile, project)
                }
            }
            if (analysisScope == null && virtualFile != null) {
                analysisScope = AnalysisScope(project, listOf(virtualFile))
                projectDir = isBaseDir(virtualFile, project)
            }
            if (analysisScope == null) {
                projectDir = true
                analysisScope = AnalysisScope(project)
            }
        }
        if (e.inputEvent is KeyEvent) {
            inspectForKeyEvent(project, managerEx, toolWrappers, psiElement, psiFile, virtualFile, analysisScope)
            return
        }
        val element = psiFile ?: psiElement
        analysisScope.isIncludeTestSource = false
        analysisScope.setSearchInLibraries(true)
        initContext(this, toolWrappers, managerEx, element, projectDir, analysisScope)
        analyze(project, analysisScope)
    }

    private fun isBaseDir(file: VirtualFile, project: Project): Boolean {
        if (file.canonicalPath == null || project.basePath == null) {
            return false
        }
        return project.basePath == file.canonicalPath
    }

    public override fun analyze(project: Project, scope: AnalysisScope) {
        super.analyze(project, scope)
    }

    private fun inspectForKeyEvent(
        project: Project,
        managerEx: InspectionManagerEx,
        toolWrappers: List<InspectionToolWrapper<*, *>>,
        psiElement: PsiElement?,
        psiFile: PsiFile?,
        virtualFile: VirtualFile?,
        analysisScope: AnalysisScope
    ) {
        var module: Module? = null
        if (virtualFile != null && project.projectFile!! != virtualFile) {
            module = ModuleUtilCore.findModuleForFile(virtualFile, project)
        }

        val uiOptions = AnalysisUIOptions.getInstance(project)
        uiOptions.ANALYZE_TEST_SOURCES = false
        val items = BaseAnalysisActionDialog.standardItems(
            project,
            analysisScope,
            Optional.ofNullable(module?.name)
                .map { ModuleManager.getInstance(project).findModuleByName(it) }
                .orElse(null),
            psiElement
        )
        val dialog = BaseAnalysisActionDialog(
            "Select Analyze Scope",
            "Analyze Scope",
            project,
            items,
            uiOptions,
            true
        )

        if (!dialog.showAndGet()) {
            return
        }
        val scope = dialog.getScope(analysisScope)
        scope.setSearchInLibraries(true)
        val element = psiFile ?: psiElement
        initContext(this, toolWrappers, managerEx, element, dialog.isProjectScopeSelected, scope)
        analyze(project, scope)
    }

    override fun update(e: AnActionEvent) {
        e.presentation.text = P3cBundle.getMessage("com.alibaba.p3c.idea.action.AliInspectionAction.text")
    }

    companion object {
        private fun getTitle(element: PsiElement?, isProjectScopeSelected: Boolean): String? {
            if (element == null) {
                return null
            }
            if (isProjectScopeSelected) {
                return "Project"
            }
            if (element is PsiFileSystemItem) {
                return VfsUtilCore.getRelativePath(element.virtualFile, element.project.projectFile!!)
            }
            return null
        }

        fun initContext(
            action: AliPmdCodeInspectionAction,
            toolWrapperList: List<InspectionToolWrapper<*, *>>,
            managerEx: InspectionManagerEx,
            psiElement: PsiElement?,
            projectScopeSelected: Boolean,
            scope: AnalysisScope
        ) {
            val title = getTitle(psiElement, projectScopeSelected)
            val model = InspectionProfileService.createSimpleProfile(toolWrapperList, managerEx, psiElement)
            title?.let {
                model.name = it
            }
            action.myExternalProfile = model
        }
    }
}
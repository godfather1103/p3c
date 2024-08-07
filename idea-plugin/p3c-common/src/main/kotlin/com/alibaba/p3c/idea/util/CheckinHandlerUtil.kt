package com.alibaba.p3c.idea.util

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.GeneratedSourcesFilter
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.containers.ContainerUtil

object CheckinHandlerUtil {

    private fun isGeneratedOrExcluded(project: Project, file: VirtualFile): Boolean {
        val fileIndex = ProjectFileIndex.getInstance(project)
        return fileIndex.isExcluded(file) || GeneratedSourcesFilter.isGeneratedSourceByAnyFilter(file, project)
    }

    fun filterOutGeneratedAndExcludedFiles(
        files: Collection<VirtualFile>,
        project: Project
    ): List<VirtualFile> {
        return ContainerUtil.filter(
            files
        ) { file: VirtualFile? ->
            !isGeneratedOrExcluded(
                project,
                file!!
            )
        }
    }
}
package com.alibaba.p3c.idea.service

import com.alibaba.p3c.idea.config.P3cConfig
import com.alibaba.p3c.idea.inspection.AliPmdInspectionInvoker
import com.alibaba.p3c.idea.pmd.SourceCodeProcessor
import com.alibaba.p3c.idea.util.withLockNotInline
import com.alibaba.smartfox.idea.common.util.getService
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.*
import com.intellij.psi.PsiManager
import net.sourceforge.pmd.RuleViolation
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantReadWriteLock

@Service(Service.Level.PROJECT)
class FileListenerService(val project: Project) {
    private val listener: VirtualFileListener
    private val javaExtension = ".java"
    private val velocityExtension = ".vm"

    private val lock = ReentrantReadWriteLock()
    private val readLock = lock.readLock()
    private val writeLock = lock.writeLock()

    private val fileContexts = ConcurrentHashMap<String, FileContext>()

    private val p3cConfig = P3cConfig::class.java.getService()

    init {
        listener = object : VirtualFileListener {
            override fun contentsChanged(event: VirtualFileEvent) {
                val path = getFilePath(event) ?: return
                PsiManager.getInstance(project).findFile(event.file) ?: return
                if (!p3cConfig.ruleCacheEnable) {
                    AliPmdInspectionInvoker.refreshFileViolationsCache(event.file)
                }
                if (!p3cConfig.astCacheEnable) {
                    SourceCodeProcessor.invalidateCache(path)
                }
                SourceCodeProcessor.invalidUserTrigger(path)
                fileContexts[path]?.ruleViolations = null
            }

            override fun fileDeleted(event: VirtualFileEvent) {
                val path = getFilePath(event)
                path?.let {
                    SourceCodeProcessor.invalidateCache(it)
                    removeFileContext(it)
                }
                super.fileDeleted(event)
            }

            override fun fileMoved(event: VirtualFileMoveEvent) {
                val path = getFilePath(event)
                path?.let {
                    SourceCodeProcessor.invalidateCache(it)
                    removeFileContext(it)
                }
                super.fileMoved(event)
            }

            private fun getFilePath(event: VirtualFileEvent): String? {
                val path = event.file.canonicalPath
                if (path == null || !(path.endsWith(javaExtension) || path.endsWith(velocityExtension))) {
                    return null
                }
                return path
            }
        }
    }

    fun projectOpened(project: Project) {
        VirtualFileManager.getInstance().addVirtualFileListener(listener, project)
    }

    fun projectClosed(project: Project) {
        VirtualFileManager.getInstance().removeVirtualFileListener(listener)
    }

    data class FileContext(
        val lock: ReentrantReadWriteLock,
        var ruleViolations: Map<String, List<RuleViolation>>? = null
    )

    fun removeFileContext(path: String) {
        fileContexts.remove(path)
    }

    fun getFileContext(virtualFile: VirtualFile?): FileContext? {
        val file = virtualFile?.canonicalPath ?: return null
        val result = readLock.withLockNotInline {
            fileContexts[file]
        }
        if (result != null) {
            return result
        }
        return writeLock.withLockNotInline {
            val finalContext = fileContexts[file]
            if (finalContext != null) {
                return@withLockNotInline finalContext
            }
            val lock = ReentrantReadWriteLock()
            FileContext(
                lock = lock
            ).also {
                fileContexts[file] = it
            }
        }
    }
}
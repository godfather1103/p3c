package com.alibaba.p3c.idea.service

import com.alibaba.p3c.idea.config.P3cConfig
import com.alibaba.p3c.idea.inspection.AliPmdInspectionInvoker
import com.alibaba.p3c.idea.pmd.SourceCodeProcessor
import com.alibaba.p3c.idea.util.withLockNotInline
import com.alibaba.smartfox.idea.common.util.getService
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.*
import com.intellij.openapi.vfs.newvfs.events.VFileContentChangeEvent
import com.intellij.openapi.vfs.newvfs.events.VFileDeleteEvent
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.intellij.openapi.vfs.newvfs.events.VFileMoveEvent
import com.intellij.psi.PsiManager
import net.sourceforge.pmd.RuleViolation
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantReadWriteLock

interface IFileListenerService {
    fun contentsChanged(event: VFileContentChangeEvent)
    fun fileDeleted(event: VFileDeleteEvent)
    fun fileMoved(event: VFileMoveEvent)
}

@Service(Service.Level.PROJECT)
class FileListenerService(val project: Project) : IFileListenerService {
    private val asyncListener: AsyncFileListener
    private val javaExtension = ".java"
    private val velocityExtension = ".vm"

    private val lock = ReentrantReadWriteLock()
    private val readLock = lock.readLock()
    private val writeLock = lock.writeLock()

    private val fileContexts = ConcurrentHashMap<String, FileContext>()

    private val p3cConfig = P3cConfig::class.java.getService()

    init {
        asyncListener = AsyncFileListener {
            object : AsyncFileListener.ChangeApplier {
                override fun afterVfsChange() {
                    it.forEach { event ->
                        when (event) {
                            is VFileContentChangeEvent -> contentsChanged(event)
                            is VFileDeleteEvent -> fileDeleted(event)
                            is VFileMoveEvent -> fileMoved(event)
                        }
                    }
                }
            }
        }
    }

    fun projectOpened(project: Project) {
        VirtualFileManager.getInstance().addAsyncFileListener(asyncListener, project)
    }

    fun projectClosed() {
        fileContexts.clear()
    }

    data class FileContext(
        val lock: ReentrantReadWriteLock,
        var ruleViolations: Map<String, List<RuleViolation>>? = null
    )

    private fun removeFileContext(path: String) {
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

    private fun getFilePath(event: VFileEvent): String? {
        val path = event.file?.canonicalPath
        if (path == null || !(path.endsWith(javaExtension) || path.endsWith(velocityExtension))) {
            return null
        }
        return path
    }

    override fun contentsChanged(event: VFileContentChangeEvent) {
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

    override fun fileDeleted(event: VFileDeleteEvent) {
        getFilePath(event)?.let {
            SourceCodeProcessor.invalidateCache(it)
            removeFileContext(it)
        }
    }

    override fun fileMoved(event: VFileMoveEvent) {
        getFilePath(event)?.let {
            SourceCodeProcessor.invalidateCache(it)
            removeFileContext(it)
        }
    }
}
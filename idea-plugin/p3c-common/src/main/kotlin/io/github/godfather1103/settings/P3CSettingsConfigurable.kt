package io.github.godfather1103.settings

import com.alibaba.p3c.idea.config.P3cConfig
import com.alibaba.p3c.pmd.lang.java.util.namelist.NameListConfig
import com.intellij.openapi.options.Configurable
import io.github.godfather1103.service.BaseNameListServiceExt
import javax.swing.JComponent


/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2023</p>
 * <p>Company:      https://github.com/godfather1103</p>
 *
 * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * @version 1.0
 * @date 创建时间：2023/8/22 09:44
 * @since 1.0
 */
class P3CSettingsConfigurable : Configurable {

    private lateinit var mySettingsComponent: P3CSettingsComponent

    override fun createComponent(): JComponent {
        mySettingsComponent = P3CSettingsComponent()
        return mySettingsComponent.getPanel()
    }

    override fun isModified(): Boolean {
        return P3cConfig.getInstance()
            .customNamelistProperties != mySettingsComponent.getInDataText()
    }

    override fun apply() {
        val inData = mySettingsComponent.getInDataText()
        P3cConfig.getInstance().customNamelistProperties = inData
        val service = NameListConfig.NAME_LIST_SERVICE
        if (service is BaseNameListServiceExt) {
            if (service.oldData != inData) {
                service.resetData(inData, true)
            }
        }
    }

    override fun reset() {
        mySettingsComponent.setInDataText(P3cConfig.getInstance().customNamelistProperties)
    }

    override fun getDisplayName() = "P3C Configure"
}
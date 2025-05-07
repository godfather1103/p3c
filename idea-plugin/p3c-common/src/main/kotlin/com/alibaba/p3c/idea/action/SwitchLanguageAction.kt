/*
 * Copyright 1999-2017 Alibaba Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.p3c.idea.action

import com.alibaba.p3c.idea.config.P3cConfig
import com.alibaba.p3c.idea.i18n.P3cBundle
import com.alibaba.smartfox.idea.common.util.BalloonNotifications
import com.intellij.ide.actions.RestartIdeAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ex.ApplicationManagerEx

/**
 *
 *
 * @author caikang
 * @date 2017/06/20
 */
class SwitchLanguageAction : RestartIdeAction() {

    private val textKey = "com.alibaba.p3c.action.switch_language.text"

    override fun actionPerformed(e: AnActionEvent) {
        P3cConfig.getInstance().toggleLanguage()
        BalloonNotifications.showSuccessNotification(
            P3cBundle.getMessage("$textKey.success"), e.project,
            { notification, _ ->
                notification.expire()
                ApplicationManagerEx.getApplicationEx().restart(false)
            }, sticky = true
        )
        super.actionPerformed(e)
    }

    override fun update(e: AnActionEvent) {
        e.presentation.text = P3cBundle.getMessage("$textKey.cur_${P3cConfig.getInstance().locale}")
    }
}

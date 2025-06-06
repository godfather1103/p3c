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
package com.alibaba.p3c.idea.config

import com.alibaba.p3c.pmd.I18nResources
import com.alibaba.smartfox.idea.common.util.getService
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import java.util.*

/**
 *
 *
 * @author caikang
 * @date 2017/06/19
 */
@State(name = "P3cConfig", storages = [Storage("smartfox_p3c.xml")])
class P3cConfig : PersistentStateComponent<P3cConfig> {

    var customNamelistProperties = ""

    var astCacheTime = 1000L
    var astCacheEnable = true

    var ruleCacheTime = 1000L
    var ruleCacheEnable = false

    var analysisBeforeCheckin = false

    var locale: String = ""
        get() {
            if (field.isEmpty()) {
                val lang = Locale.getDefault().language
                return if (lang != Locale.ENGLISH.language && lang != Locale.CHINESE.language) {
                    Locale.ENGLISH.language
                } else Locale.getDefault().language
            }

            return field
        }

    fun toggleLanguage() {
        locale = if (localeEn == locale) localeZh else localeEn
    }

    override fun getState(): P3cConfig {
        return this
    }

    override fun loadState(state: P3cConfig) {
        XmlSerializerUtil.copyBean(state, this)
        I18nResources.changeLanguage(locale)
    }

    companion object {
        val localeEn: String = Locale.ENGLISH.language
        val localeZh: String = Locale.CHINESE.language

        @JvmStatic
        fun getInstance() = P3cConfig::class.java.getService()
    }
}

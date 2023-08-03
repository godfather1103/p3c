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
package icons

import com.intellij.openapi.util.IconLoader.getIcon

/**
 * @author caikang
 * @date 2016/12/28
 */
class P3cIcons private constructor() {

    init {
        throw AssertionError(
            "icons.P3cIcons"
                    + " instances for you!"
        )
    }

    companion object {

        @JvmField
        val ANALYSIS_ACTION = getIcon("/icons/ali-ide-run.png", P3cIcons::class.java)

        @JvmField
        val PROJECT_INSPECTION_ON = getIcon("/icons/qiyong.png", P3cIcons::class.java)

        @JvmField
        val PROJECT_INSPECTION_OFF = getIcon("/icons/tingyong.png", P3cIcons::class.java)

        @JvmField
        val LANGUAGE = getIcon("/icons/language.png", P3cIcons::class.java)

        @JvmField
        val ALIBABA = getIcon("/icons/alibaba.png", P3cIcons::class.java)
    }
}

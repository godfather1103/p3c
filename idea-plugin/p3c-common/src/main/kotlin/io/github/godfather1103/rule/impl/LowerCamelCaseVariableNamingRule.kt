package io.github.godfather1103.rule.impl

import com.alibaba.p3c.pmd.lang.java.rule.naming.LowerCamelCaseVariableNamingRule
import io.github.godfather1103.rule.IModifyRuleValue
import io.github.godfather1103.service.BaseNameListServiceExt

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2023</p>
 * <p>Company:      https://github.com/godfather1103</p>
 *
 * @author  作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * @date 创建时间：2023/8/22 16:36
 * @version 1.0
 * @since  1.0
 */
class LowerCamelCaseVariableNamingRule : IModifyRuleValue {

    override fun needModifyOnInit(): Boolean {
        return true
    }

    override fun modifyValue(base: BaseNameListServiceExt, key: String) {
        when (key) {
            "WHITE_LIST" -> {
                val list = base.getNameList(className(), key).distinct()
                if (list.isNotEmpty()) {
                    LowerCamelCaseVariableNamingRule.makePattern(list)
                }
            }
        }
    }
}
package io.github.godfather1103.rule.impl

import io.github.godfather1103.rule.IModifyRuleValue
import io.github.godfather1103.service.BaseNameListServiceExt

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2023</p>
 * <p>Company:      https://github.com/godfather1103</p>
 *
 * @author  作者: Jack Chu E-mail: chuchuanbao@gmail.com<BR>
 * 创建时间：2023-08-22 22:58
 * @version 1.0
 * @since  1.0
 */
class ClassNamingShouldBeCamelRule : IModifyRuleValue {
    override fun modifyValue(base: BaseNameListServiceExt, key: String) {
        when (key) {
            "CLASS_NAMING_WHITE_LIST" ->
                com.alibaba.p3c.pmd.lang.java.rule.naming.ClassNamingShouldBeCamelRule::class.java
                    .declaredFields.find { it.name == key }?.apply {
                        isAccessible = true
                        val set = get(null) as MutableList<String>
                        set.clear()
                        base.getNameList(className(), key)
                            .forEach { set.add(it) }
                    }
        }
    }
}
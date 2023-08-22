package io.github.godfather1103.rule.impl

import com.alibaba.p3c.pmd.lang.java.util.PojoUtils
import io.github.godfather1103.rule.IModifyRuleValue
import io.github.godfather1103.service.BaseNameListServiceExt

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2023</p>
 * <p>Company:      https://github.com/godfather1103</p>
 *
 * @author  作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * @date 创建时间：2023/8/22 19:00
 * @version 1.0
 * @since  1.0
 */
class PojoMustOverrideToStringRule : IModifyRuleValue {
    override fun modifyValue(base: BaseNameListServiceExt, key: String) {
        when (key) {
            "POJO_SUFFIX_SET" -> PojoUtils::class.java
                .declaredFields.find { it.name == key }?.apply {
                    isAccessible = true
                    val list = get(null) as MutableList<String>
                    list.clear()
                    base.getNameList(className(), key)
                        .forEach { list.add(it) }
                }
        }

    }
}
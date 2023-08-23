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
class MethodReturnWrapperTypeRule : IModifyRuleValue {
    override fun modifyValue(base: BaseNameListServiceExt, key: String) {
        when (key) {
            "PRIMITIVE_TYPE_TO_WAPPER_TYPE" ->
                com.alibaba.p3c.pmd.lang.java.rule.exception.MethodReturnWrapperTypeRule::class.java
                    .declaredFields.find { it.name == key }?.apply {
                        isAccessible = true
                        val map = get(null) as MutableMap<String, String>
                        map.clear()
                        base.getNameMap(className(), key, String::class.java, String::class.java)
                            .forEach { (t, u) -> map[t] = u }
                    }
        }
    }
}
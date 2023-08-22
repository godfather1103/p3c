package io.github.godfather1103.rule.impl

import com.alibaba.p3c.pmd.lang.java.util.PojoUtils
import io.github.godfather1103.rule.IModifyRuleValue
import io.github.godfather1103.service.BaseNameListServiceExt

class PojoMustOverrideToStringRule : IModifyRuleValue {
    override fun modifyValue(base: BaseNameListServiceExt, key: String) {
        PojoUtils::class.java.declaredFields.find { it.name == "POJO_SUFFIX_SET" }?.apply {
            isAccessible = true
            val list = get(null) as MutableList<String>
            list?.clear()
            base.getNameList(className(), key)?.forEach { list.add(it) }
        }
    }
}
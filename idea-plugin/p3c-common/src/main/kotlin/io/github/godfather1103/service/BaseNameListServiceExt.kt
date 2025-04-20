package io.github.godfather1103.service

import com.alibaba.p3c.pmd.lang.java.util.namelist.NameListService
import com.alibaba.p3c.pmd.lang.java.util.namelist.NameListServiceImpl
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.godfather1103.rule.IModifyRuleValue
import org.slf4j.LoggerFactory
import java.io.IOException
import java.io.StringReader
import java.util.*

/**
 * <p>Title:        Godfather1103's Github</p>
 * <p>Copyright:    Copyright (c) 2023</p>
 * <p>Company:      https://github.com/godfather1103</p>
 *
 * @author  作者: Jack Chu E-mail: chuchuanbao@gmail.com
 * @date 创建时间：2023/8/22 13:56
 * @version 1.0
 * @since  1.0
 */
abstract class BaseNameListServiceExt : NameListService {

    private val modifyList by lazy {
        ServiceLoader
            .load(IModifyRuleValue::class.java, BaseNameListServiceExt::class.java.classLoader)
            .toList()
    }

    private var properties: Properties = Properties()

    var oldData = ""

    private val logger = LoggerFactory.getLogger(BaseNameListServiceExt::class.java)

    fun resetData(inDate: String, isReset: Boolean = false) {
        try {
            resetProperties(inDate)
            oldData = inDate
            modifyRuleValue(this, isReset)
        } catch (e: Exception) {
            logger.warn("resetData By [{}] error", inDate, e)
            properties.clear()
        }
    }

    open fun modifyRuleValue(base: BaseNameListServiceExt, isReset: Boolean = false) {
        properties.keys.forEach { key ->
            val keys = (key as String).split(SEPARATOR.toRegex(), 2)
            modifyList.forEach {
                if (isReset || it.needModifyOnInit()) {
                    if (it.className() == keys[0]) {
                        try {
                            it.modifyValue(base, keys[1])
                        } catch (e: Exception) {
                            logger.warn("className[{}] modifyValue error", it.className(), e)
                        }
                    }
                }
            }
        }
    }

    /**
     * resetProperties<BR></BR>
     *
     * @param inData 参数
     * @return void
     * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
     * @date 创建时间：2023/8/22 11:19
     */
    open fun resetProperties(inData: String) {
        properties.clear()
        properties = initProperties(inData)
    }

    override fun getNameList(className: String, name: String): List<String> {
        val key = className + SEPARATOR + name
        if (!properties.containsKey(key)) {
            return nameListService.getNameList(className, name)
        }
        val gson = Gson()
        return gson.fromJson(
            properties[key] as String?,
            object : TypeToken<List<String>>() {}.type
        )
    }

    override fun <K, V> getNameMap(className: String, name: String, kClass: Class<K>, vClass: Class<V>): Map<K, V> {
        val key = className + SEPARATOR + name
        if (!properties.containsKey(key)) {
            return nameListService.getNameMap(className, name, kClass, vClass)
        }
        val gson = Gson()
        return gson.fromJson(
            properties[key] as String?,
            object : TypeToken<Map<K, V>>() {}.type
        )
    }

    companion object {
        private const val SEPARATOR = "_"
        private val nameListService = NameListServiceImpl()

        /**
         * 初始化properties<BR>
         * @param string 输入的数据
         * @return 对应的properties对象
         * @throws IllegalStateException 相关异常
         * @author 作者: Jack Chu E-mail: chuchuanbao@gmail.com
         * @date 创建时间：2023/8/22 13:53
         */
        fun initProperties(string: String): Properties {
            val props = Properties()
            try {
                props.load(StringReader(string))
            } catch (ex: IOException) {
                throw IllegalStateException("Load namelist.properties fail", ex)
            }
            return props
        }
    }
}

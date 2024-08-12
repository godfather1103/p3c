## 安装前请卸载已安装的官方插件避免冲突。

# 前言

因为阿里官方长时间未修复相关bug，所以只能fork一份，修复在使用过程中遇到的bug。

- fork节点为[6c59c8c3](https://github.com/alibaba/p3c/commit/6c59c8c36ecd8722c712d5685b8c3822c1c8b030)

插件已经上传到了jetbrains的插件市场  
欢迎安装[https://plugins.jetbrains.com/plugin/22381-alibaba-java-coding-guidelines-fix-some-bug-](https://plugins.jetbrains.com/plugin/22381-alibaba-java-coding-guidelines-fix-some-bug-)

# 源码地址

- [Github](https://github.com/godfather1103/p3c)
- [Gitee](https://gitee.com/godfather1103/p3c)

# 插件版本号说明

> 迭代版本 + idea版本 + P3C_PMD版本
 
# namelist.properties说明

```properties
# 常量字段名日志对象字段名
ConstantFieldShouldBeUpperCaseRule_LOG_VARIABLE_TYPE_SET=["Log","Logger"]
# 常量字段名的白名单
ConstantFieldShouldBeUpperCaseRule_WHITE_LIST=["serialVersionUID"]
# 驼峰需要忽略的专有名词
LowerCamelCaseVariableNamingRule_WHITE_LIST=["DAOImpl"]
# pojo的后缀
PojoMustOverrideToStringRule_POJO_SUFFIX_SET=["DO","DTO","VO","BO"]
# 忽略的魔法值列表
UndefineMagicConstantRule_LITERAL_WHITE_LIST=["0","1","\\\"\\\"","0.0","1.0","-1","0L","1L"]
# 包装类映射
MethodReturnWrapperTypeRule_PRIMITIVE_TYPE_TO_WAPPER_TYPE={"int":"Integer","boolean":"Boolean","float":"Float","double":"Double","byte":"Byte","short":"Short","long":"Long","char":"Character"}
# 需要初始化大小的对象
CollectionInitShouldAssignCapacityRule_COLLECTION_TYPE=["HashMap","ConcurrentHashMap"]
# 类名中需要排除的专有名词
ClassNamingShouldBeCamelRule_CLASS_NAMING_WHITE_LIST=["Hbase","HBase","ID"]

```

# 已修复的issues

### 本地

- [issues-43](https://github.com/godfather1103/p3c/issues/43)
- [issues-29](https://github.com/godfather1103/p3c/issues/29)
- [issues-27](https://github.com/godfather1103/p3c/issues/27)

### 官方

- [issues-898](https://github.com/alibaba/p3c/issues/898)
- [issues-900](https://github.com/alibaba/p3c/issues/900)

# 群组(Group)

使用过程中如有疑问，可以加群提问

![WXQ](pic/WXQ-1-300.jpg)

个人微信，添加时备注：p3c

![GR](pic/GR-300.jpg)


# 捐赠(Donate)

你的馈赠将助力我更好的去贡献，谢谢！  
Your gift will help me to contribute better, thank you!

[PayPal](https://paypal.me/godfather1103?locale.x=zh_XC)

支付宝(Alipay)  
![支付宝](pic/hb-300.png)
![支付宝](pic/Alipay-300.png)

微信(WeChat)  
![微信支付](pic/WeChat-300.png)

# Star History

![Star History Chart](https://api.star-history.com/svg?repos=godfather1103/p3c&type=Date)

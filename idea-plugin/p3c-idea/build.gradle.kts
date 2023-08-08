plugins {
    id("org.jetbrains.intellij")
}

val ideaVersion = rootProject.ext.get("ideaVersion") as String
val yearVersion = rootProject.ext.get("yearVersion") as Int
val noVersion = rootProject.ext.get("noVersion") as Int
val myPlugins = rootProject.ext.get("myPlugins") as Set<*>

intellij {
    version.set(ideaVersion)
    plugins.set(myPlugins)
    pluginName.set("${property("plugin_name")}")
    updateSinceUntilBuild.set(false)
    sandboxDir.set("idea-sandbox")
}

tasks {
    patchPluginXml {
        sinceBuild.set("${yearVersion}${noVersion}.0")
        untilBuild.set("${yearVersion}${noVersion}.*")
        pluginId.set("io.github.godfather1103.alibaba.p3c")
        pluginDescription.set(
            """
<h1>English Readme：</h1>
<h2>The plugin conflicts with the official plugin. Please uninstall the original plugin before installing this plugin</h2>
<p>Alibaba Java Coding Guidelines plugin support.Fix some bug.such as <a href="https://github.com/alibaba/p3c/issues/898">issues-898</a>,<a href="https://github.com/alibaba/p3c/issues/900">issues-900</a></p>
<p>Official plugin URL: <a href="https://plugins.jetbrains.com/plugin/10046-alibaba-java-coding-guidelines">Alibaba Java Coding Guidelines</a></p>

<h1>中文说明：</h1>
<h2>插件与官方插件会冲突，请先卸载原插件，再安装本插件</h2>
<p>阿里巴巴Java编码规范检查插件。修复了一些官方一直未修复的Bug。如 <a href="https://github.com/alibaba/p3c/issues/898">issues-898</a>,<a href="https://github.com/alibaba/p3c/issues/900">issues-900</a></p>
<p>官方插件地址：<a href="https://plugins.jetbrains.com/plugin/10046-alibaba-java-coding-guidelines">Alibaba Java Coding Guidelines</a></p>

        """.trimIndent()
        )
        changeNotes.set(
            """
        <ul>
        1.1
        <li>优化对于2023.2的API调度</li>
        <li>优化对应分析结果信息分组</li>
        </ul>
        <ul>
        1.0
        <li>fix(<a href="https://github.com/alibaba/p3c/issues/898">issues-898</a>): 1、修复2022.1中检测到缺失override注解出现异常；2、修复编译异常</li>
        <li>fix(<a href="https://github.com/alibaba/p3c/issues/900">issues-900</a>): 修复驼峰等自动修改异常</li>
        </ul>
        """.trimIndent()
        )
    }

    publishPlugin {
        project.findProperty("ORG_GRADLE_PROJECT_intellijPublishToken")?.let {
            token.set(it as String)
        }
    }

    signPlugin {
        project.findProperty("signing.certificateChainFile")?.let {
            certificateChainFile.set(file(it as String))
        }
        project.findProperty("signing.privateKeyFile")?.let {
            privateKeyFile.set(file(it as String))
        }
        project.findProperty("signing.password")?.let {
            password.set(it as String)
        }
    }

    initializeIntelliJPlugin {
        offline.set(true)
    }

    downloadZipSigner {
        cliPath.set("${project.allprojects.find { it.name == "p3c-idea" }!!.projectDir.absolutePath}/tools/marketplace-zip-signer-cli.jar")
    }

}

version = "${property("plugin_version")}-${ideaVersion}-${property("p3c_pmd_version")}"

dependencies {
    implementation("org.freemarker:freemarker:2.3.25-incubating")
    implementation(project(":p3c-common"))
    implementation("org.javassist:javassist:3.21.0-GA")
}
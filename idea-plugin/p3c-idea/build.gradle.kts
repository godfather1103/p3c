plugins {
    id("org.jetbrains.intellij.platform")
}

val ideaVersion = rootProject.ext.get("ideaVersion") as String
val yearVersion = rootProject.ext.get("yearVersion") as Int
val noVersion = rootProject.ext.get("noVersion") as Int
val myPlugins = rootProject.ext.get("myPlugins") as List<*>
val publishChannel = project.findProperty("publishChannel") as String

version = if (publishChannel.isNotEmpty()) {
    "${property("plugin_version")}-${ideaVersion}-${property("p3c_pmd_version")}-${publishChannel}"
} else {
    "${property("plugin_version")}-${ideaVersion}-${property("p3c_pmd_version")}"
}


repositories {
    mavenLocal()
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    implementation(project(":p3c-common"))
    intellijPlatform {
        intellijIdea(ideaVersion)
        bundledPlugins(myPlugins.map { it.toString() })
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
    }
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "${yearVersion}${noVersion}.0"
        }
        name.set("${property("plugin_name")}")
        id.set("io.github.godfather1103.alibaba.p3c")
        description.set(
            """
Alibaba Java Coding Guidelines plugin
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
        2.3
        <li>升级asm依赖</li>
        </ul>
        """.trimIndent()
        )
    }
    sandboxContainer.set(file("idea-sandbox/${ideaVersion}"))
    publishing {
        project.findProperty("ORG_GRADLE_PROJECT_intellijPublishToken")?.let {
            token.set(it as String)
        }
        if (publishChannel.isNotEmpty()) {
            channels.set(listOf(publishChannel))
        } else if (ideaVersion.contains("EAP-SNAPSHOT")) {
            channels.set(listOf("beta"))
        }
    }
    signing {
        cliPath = file("${project.projectDir.absolutePath}/tools/marketplace-zip-signer-cli.jar")
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
}

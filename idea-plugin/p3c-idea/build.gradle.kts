plugins {
    id("org.jetbrains.intellij")
}

val ideaVersion = rootProject.ext.get("ideaVersion") as String
val yearVersion = rootProject.ext.get("yearVersion") as Int
val noVersion = rootProject.ext.get("noVersion") as Int
val myPlugins = rootProject.ext.get("myPlugins") as Set<*>
val publishChannel = project.findProperty("publishChannel") as String

intellij {
    version.set(ideaVersion)
    plugins.set(myPlugins)
    pluginName.set("${property("plugin_name")}")
    updateSinceUntilBuild.set(false)
    sandboxDir.set("idea-sandbox/${ideaVersion}")
}

tasks {
    patchPluginXml {
        sinceBuild.set("${yearVersion}${noVersion}.0")
//        untilBuild.set("${yearVersion}${noVersion}.*")
        pluginId.set("io.github.godfather1103.alibaba.p3c")
        changeNotes.set(
            """
        <ul>
        2.2
        <li> fix <a href="https://github.com/godfather1103/p3c/issues/72">issues-72</a></li>
        </ul>
        """.trimIndent()
        )
    }

    publishPlugin {
        project.findProperty("ORG_GRADLE_PROJECT_intellijPublishToken")?.let {
            token.set(it as String)
        }
        if (publishChannel.isNotEmpty()) {
            channels.set(listOf(publishChannel))
        } else if (ideaVersion.contains("EAP-SNAPSHOT")) {
            channels.set(listOf("beta"))
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

version = if (publishChannel.isNotEmpty()) {
    "${property("plugin_version")}-${ideaVersion}-${property("p3c_pmd_version")}-${publishChannel}"
} else {
    "${property("plugin_version")}-${ideaVersion}-${property("p3c_pmd_version")}"
}

dependencies {
    implementation(project(":p3c-common"))
}
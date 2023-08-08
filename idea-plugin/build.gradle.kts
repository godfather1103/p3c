plugins {
    java
}

buildscript {
    repositories {
        mavenLocal()
        maven {
            url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        }
        gradlePluginPortal()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${property("kotlin_version")}")
        classpath("org.jetbrains.intellij.plugins:gradle-intellij-plugin:${property("gradle_jetbrains_version")}")
    }
}

val ideaVersion = property("idea_version") as String

val yearVersion = ideaVersion
    .split(".")
    .first()
    .substring(2)
    .toInt()

val noVersion = ideaVersion
    .substring(ideaVersion.indexOf(".") + 1)
    .toInt()

val myPlugins = when (yearVersion) {
    in 23..Int.MAX_VALUE -> setOf("vcs-git", "java")
    22 -> if (noVersion == 3) setOf("vcs-git", "java") else setOf("git4idea", "java")
    in 19..21 -> setOf("git4idea", "java")
    else -> setOf("git4idea")
}

ext["ideaVersion"] = ideaVersion
ext["yearVersion"] = yearVersion
ext["noVersion"] = noVersion
ext["myPlugins"] = myPlugins

subprojects {

    group = "com.alibaba.p3c.idea"

    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "maven-publish")


    java.sourceCompatibility = JavaVersion.VERSION_11
    java.targetCompatibility = JavaVersion.VERSION_11

    tasks.compileJava {
        options.encoding = "UTF-8"
    }

    configurations.all {
        resolutionStrategy {
            cacheChangingModulesFor(0, TimeUnit.SECONDS)
        }
    }

    repositories {
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        testImplementation("junit:junit:4.11")
    }
}
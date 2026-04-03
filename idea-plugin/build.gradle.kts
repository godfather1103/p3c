import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

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
        classpath("org.jetbrains.intellij.platform:org.jetbrains.intellij.platform.gradle.plugin:${property("gradle_jetbrains_version")}")
    }
}

val ideaVersion = System.getProperty(
    "idea_version",
    property("idea_version") as String
)!!
println("构建的目标版本为:${ideaVersion}")

val first = ideaVersion.split("[.-]".toRegex()).first().toInt()
val yearVersion = first.let { if (it > 2000) it % 100 else it / 10 }
val noVersion = if (first < 2000) first % 10 else ideaVersion
    .substring(ideaVersion.indexOf(".") + 1)
    .toInt()

val myPlugins = listOf("com.intellij.java", "Git4Idea")

ext["ideaVersion"] = ideaVersion
ext["yearVersion"] = yearVersion
ext["noVersion"] = noVersion
ext["myPlugins"] = myPlugins

subprojects {

    group = "com.alibaba.p3c.idea"

    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "maven-publish")

    tasks.withType<KotlinJvmCompile> {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
            freeCompilerArgs.set(listOf("-Xjsr305=strict"))
        }
    }

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
        testImplementation("junit:junit:4.13.2")
    }
}
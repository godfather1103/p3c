plugins {
    id("org.jetbrains.intellij.platform")
    id("signing")
}

tasks.javadoc {
    (options as StandardJavadocDocletOptions).tags = listOf("date")
}

java {
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenLocal()
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

val ideaVersion = rootProject.ext.get("ideaVersion") as String
val myPlugins = rootProject.ext.get("myPlugins") as List<*>
val yearVersion = rootProject.ext.get("yearVersion") as Int
val noVersion = rootProject.ext.get("noVersion") as Int
if (yearVersion <= 22) {
    sourceSets {
        main {
            kotlin {
                srcDirs("src/main/idea-code/223")
            }
        }
    }
} else if (yearVersion == 23 && noVersion < 3) {
    sourceSets {
        main {
            kotlin {
                srcDirs("src/main/idea-code/232")
            }
        }
    }
} else {
    sourceSets {
        main {
            kotlin {
                srcDirs("src/main/idea-code/last")
            }
        }
    }
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "${yearVersion}${noVersion}.0"
        }
        name.set("${property("plugin_name")}")
    }
    sandboxContainer.set(file("${project.projectDir}/idea-sandbox/${ideaVersion}"))
}

version = "2.0.1"

ext["isReleaseVersion"] = !version.toString().endsWith("SNAPSHOT")

dependencies {
    implementation("org.freemarker:freemarker:2.3.25-incubating")
    implementation(project(":p3c-pmd"))
    implementation("org.javassist:javassist:3.21.0-GA")
    intellijPlatform {
        intellijIdea(ideaVersion)
        bundledPlugins(myPlugins.map { it.toString() })
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
    }
}

publishing {
    repositories {
        maven {
            url = if (!version.toString().uppercase().contains("SNAPSHOT")) {
                uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            } else {
                uri("https://oss.sonatype.org/content/repositories/snapshots/")
            }
            credentials {
                findProperty("ossrhUsername")?.let {
                    username = it as String
                }
                findProperty("ossrhPassword")?.let {
                    password = it as String
                }
            }
        }
    }

    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "p3c-common"
            from(components["java"])
            pom {
                name.set("p3c-common")
                description.set("P3c Idea Plugin Common.'")
                url.set("https://github.com/godfather1103/p3c")

                scm {
                    url.set("https://github.com/godfather1103/p3c")
                    connection.set("scm:git:https://github.com/godfather1103/p3c.git")
                    developerConnection.set("scm:git:https://github.com/godfather1103/p3c.git")
                }

                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }

                developers {
                    developer {
                        id.set("godfather1103")
                        name.set("Jack Chu")
                        email.set("chuchuanbao@gmail.com")
                    }
                    developer {
                        id.set("junlie")
                        name.set("Junlie")
                        email.set("sean.caikang@gmail.com")
                    }
                    developer {
                        id.set("ZengHou")
                        name.set("ZengHou")
                        email.set("fengwei1983@gmail.com")
                    }
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}


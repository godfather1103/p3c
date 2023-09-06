plugins {
    id("org.jetbrains.intellij")
    id("signing")
}

tasks.javadoc {
    (options as StandardJavadocDocletOptions).tags = listOf("date")
}

java {
    withJavadocJar()
    withSourcesJar()
}

val ideaVersion = rootProject.ext.get("ideaVersion") as String
val myPlugins = rootProject.ext.get("myPlugins") as Set<*>

intellij {
    version.set(ideaVersion)
    plugins.set(myPlugins)
    pluginName.set("${property("plugin_name")}")
    updateSinceUntilBuild.set(false)
    sandboxDir.set("${project.buildDir}/idea-sandbox/${ideaVersion}")
}

version = "2.0.1"

ext["isReleaseVersion"] = !version.toString().endsWith("SNAPSHOT")

dependencies {
    implementation("com.alibaba.p3c:p3c-pmd:${property("p3c_pmd_version")}")
    testImplementation("net.sourceforge.pmd:pmd-test:${property("pmd.version")}")
}

publishing {
    repositories {
        maven {
            if (!version.toString().toUpperCase().contains("SNAPSHOT")) {
                url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            } else {
                url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
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

tasks.initializeIntelliJPlugin {
    offline.set(true)
}
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.3.10"
    id("com.vanniktech.maven.publish") version "0.32.0"
}

group = "com.sdxmessaging"
version = "0.1.0"

repositories {
    mavenCentral()
}

object Versions {
    const val HTTP4K = "6.43.0.0"
    const val JUNIT = "6.0.3"
    const val KOTEST = "6.1.5"
}

dependencies {
    // The two modules we bridge
    api("org.http4k:http4k-template-htmlflow:${Versions.HTTP4K}")
    api("org.http4k:http4k-web-datastar:${Versions.HTTP4K}")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter-engine:${Versions.JUNIT}")
    testImplementation("io.kotest:kotest-runner-junit5:${Versions.KOTEST}")
    testImplementation("io.kotest:kotest-assertions-core:${Versions.KOTEST}")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_25)
    }
}

tasks.test {
    useJUnitPlatform()
}

// ── Publishing to Maven Central ─────────────────────────────────────────────

mavenPublishing {
    publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    pom {
        name.set("http4k-template-htmlflow-datastar")
        description.set("Type-safe Datastar attribute DSL for HtmlFlow views in http4k")
        url.set("https://github.com/sdxmessaging/http4k-template-htmlflow-datastar")

        licenses {
            license {
                name.set("Apache License 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0")
            }
        }

        developers {
            developer {
                id.set("sdxmessaging")
                name.set("SDX Messaging")
                url.set("https://www.sdxmessaging.com/")
            }
        }

        scm {
            url.set("https://github.com/sdxmessaging/http4k-template-htmlflow-datastar")
            connection.set("scm:git:https://github.com/sdxmessaging/http4k-template-htmlflow-datastar.git")
            developerConnection.set("scm:git:https://github.com/sdxmessaging/http4k-template-htmlflow-datastar.git")
        }
    }
}

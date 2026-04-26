import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.3.10"
}

group = "com.sdxmessaging"
version = "0.1.0-SNAPSHOT"

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

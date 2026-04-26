import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm")
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":"))
    implementation("org.http4k:http4k-server-helidon:6.43.0.0")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_25)
    }
}

// Runnable tasks for each example
tasks.register<JavaExec>("counter") {
    mainClass.set("com.sdxmessaging.examples.CounterKt")
    classpath = sourceSets["main"].runtimeClasspath
    description = "Run the Counter example on http://localhost:8080"
}

tasks.register<JavaExec>("activeSearch") {
    mainClass.set("com.sdxmessaging.examples.ActiveSearchKt")
    classpath = sourceSets["main"].runtimeClasspath
    description = "Run the Active Search example on http://localhost:8080"
}

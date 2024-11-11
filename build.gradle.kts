plugins {
    kotlin("jvm") version "2.0.0"
    id("code-analyzer-plugin")
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.named("compileKotlin") {
    dependsOn("analyzeCode")
}

kotlin {
    jvmToolchain(21)
}
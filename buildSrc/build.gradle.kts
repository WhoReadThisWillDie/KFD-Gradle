plugins {
    `kotlin-dsl`
    antlr
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(gradleKotlinDsl())
    antlr("org.antlr:antlr4:4.13.1")
}

tasks {
    generateGrammarSource {
        source = fileTree("src/main/antlr")
        outputDirectory = file("build/generated/antlr/main")
        arguments.add("-visitor")
    }

    compileKotlin {
        dependsOn("generateGrammarSource")
    }
}

sourceSets {
    main {
        java {
            srcDir("build/generated/antlr/main")
        }
    }
}

gradlePlugin {
    plugins {
        register("code-analyzer-plugin") {
            id = "code-analyzer-plugin"
            description =
                "A plugin that analyzes the source code and generates a report with the total number of classes, methods and strings"
            implementationClass = "CodeAnalyzerPlugin"
        }
    }
}
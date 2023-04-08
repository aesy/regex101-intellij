plugins {
    id("java")
    id("org.jetbrains.intellij") version "1.17.2"
    id("org.jetbrains.kotlinx.kover") version "0.7.6"
    id("org.jetbrains.kotlin.jvm") version "1.9.22"
}

group = "io.aesy.regex101"
version = "0.3"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("io.strikt:strikt-core:0.34.1")
}

kotlin {
    jvmToolchain(11)
}

intellij {
    pluginName = rootProject.name
    version = "2022.1.1"
    type = "IU"
    updateSinceUntilBuild = false
    plugins =
        listOf(
            "JavaScript",
            "java",
            "Kotlin",
            "IntelliLang",
            "Pythonid:221.5591.52",
            "org.toml.lang:221.5591.26",
            "org.rust.lang:0.4.179.4903-221",
            "com.jetbrains.php:221.5591.58",
            "org.jetbrains.plugins.go:221.5591.52",
        )
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }

    test {
        useJUnitPlatform()
    }

    publishPlugin {
        token = System.getenv("INTELLIJ_HUB_TOKEN")
    }
}

import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("java")
    id("org.jetbrains.intellij.platform") version "2.2.0"
    id("org.jetbrains.kotlinx.kover") version "0.9.0"
    id("org.jetbrains.kotlin.jvm") version "2.1.0"
}

group = "io.aesy.regex101"
version = "0.3"

repositories {
    mavenCentral()
    mavenLocal()

    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("io.strikt:strikt-core:0.35.1")

    // https://youtrack.jetbrains.com/issue/IJPL-159134/JUnit5-Test-Framework-refers-to-JUnit4-java.lang.NoClassDefFoundError-junit-framework-TestCase
    testImplementation("junit:junit:4.13.2")

    intellijPlatform {
        // https://www.jetbrains.com/idea/download/other.html
        intellijIdeaUltimate("2024.3.1.1")

        // https://plugins.jetbrains.com/docs/intellij/plugin-dependencies.html#ids-of-bundled-plugins
        bundledPlugin("JavaScript")
        bundledPlugin("com.intellij.java")
        bundledPlugin("org.jetbrains.kotlin")
        bundledPlugin("org.intellij.intelliLang")

        // https://github.com/JetBrains/intellij-platform-gradle-plugin/issues/1838
        bundledPlugin("com.intellij.llmInstaller")

        // https://plugins.jetbrains.com/plugin/631-python/versions
        plugin("Pythonid", "243.22562.218")

        // https://plugins.jetbrains.com/plugin/8195-toml/versions
        plugin("org.toml.lang", "243.21565.122")

        // https://plugins.jetbrains.com/plugin/22407-rust/versions
        plugin("com.jetbrains.rust", "243.22562.230")

        // https://plugins.jetbrains.com/plugin/6610-php/versions
        plugin("com.jetbrains.php", "243.22562.233")

        // https://plugins.jetbrains.com/plugin/9568-go/versions
        plugin("org.jetbrains.plugins.go", "243.22562.218")

        testFramework(TestFrameworkType.Platform)
    }
}

kotlin {
    jvmToolchain(21)
}

intellijPlatform {
    pluginConfiguration {
        name = rootProject.name
    }
}

tasks {
    wrapper {
        gradleVersion = "8.12"
    }

    test {
        useJUnitPlatform()
    }

    publishPlugin {
        token = System.getenv("INTELLIJ_HUB_TOKEN")
    }
}

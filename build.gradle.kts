import org.jetbrains.intellij.tasks.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.61"
    id("org.jetbrains.intellij") version "0.4.21"
}

group = "io.aesy.regex101"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

intellij {
    version = "IU-2020.1"
    pluginName = rootProject.name
    updateSinceUntilBuild = false
}

tasks {
    withType<KotlinCompile> {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"

        kotlinOptions {
            jvmTarget = "1.8"
            apiVersion = "1.3"
            languageVersion = "1.3"
        }
    }

    withType<PublishTask> {
        token(System.getenv("INTELLIJ_HUB_TOKEN"))
    }
}

import org.jetbrains.intellij.tasks.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    jacoco
    kotlin("jvm") version "1.3.61"
    id("org.jetbrains.intellij") version "0.4.21"
}

group = "io.aesy.regex101"
version = "0.2"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
    testImplementation("io.strikt:strikt-core:0.26.1")
}

intellij {
    version = "IU-2020.1"
    pluginName = rootProject.name
    updateSinceUntilBuild = false
    setPlugins("JavaScript", "java", "IntelliLang")
}

tasks {
    jacocoTestReport {
        reports {
            xml.isEnabled = true
        }
    }

    withType<Wrapper> {
        gradleVersion = "6.5"
    }

    withType<KotlinCompile> {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"

        kotlinOptions {
            jvmTarget = "1.8"
            apiVersion = "1.3"
            languageVersion = "1.3"
        }
    }

    withType<Test> {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)
    }

    withType<PublishTask> {
        token(System.getenv("INTELLIJ_HUB_TOKEN"))
    }
}

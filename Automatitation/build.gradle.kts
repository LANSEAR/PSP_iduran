plugins {
    kotlin("multiplatform") version "2.2.0"
    id("org.jetbrains.compose") version "1.8.0-beta02"
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.0"
}

group = "org.example"
version = "1.0.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
        withJava()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("org.jetbrains.compose.material3:material3-desktop:1.8.0-beta02")
            }
        }

        val desktopTest by getting
    }
}



compose.desktop {
    application {
        mainClass = "org.example.ui.MainScreenKt" // Ajusta a tu paquete real
        nativeDistributions {
            targetFormats(
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Msi,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
            )
            packageName = "TaskAutomationApp"
            packageVersion = "1.0.0"
        }
    }
}

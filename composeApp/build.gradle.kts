import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

version = "1.0.0"

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation("io.ktor:ktor-client-core:2.3.7")
            implementation("io.ktor:ktor-client-cio:2.3.7")
            implementation("io.ktor:ktor-client-websockets:2.3.7")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
            implementation("org.apache.pdfbox:pdfbox:2.0.31")
            implementation("com.github.anastaciocintra:escpos-coffee:4.1.0")
            implementation("org.usb4java:usb4java:1.3.0")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
            implementation("io.ktor:ktor-client-okhttp:2.3.7")

            implementation(compose.material3)


            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.7.3")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing")




        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
            implementation("org.slf4j:slf4j-simple:2.0.7")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")


            implementation("io.ktor:ktor-server-core:2.3.7")
            implementation("io.ktor:ktor-server-content-negotiation:2.3.7")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
            implementation("io.ktor:ktor-server-netty:2.3.7")
            implementation("io.ktor:ktor-server-cors-jvm:2.3.7")


            implementation(compose.material3)

            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-bom:1.7.3")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing")


        }

    }
}

configurations.all {
    resolutionStrategy {
        force(
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3",
            "org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.7.3"
        )
    }
}

compose {
    resources {
        packageOfResClass = "br.com.rotafood.res"
        generateResClass = auto
        publicResClass = true
    }
    desktop {
        application {
            mainClass = "br.com.rotafood.MainKt"
            buildTypes.release.proguard.isEnabled.set(false)
            nativeDistributions {
                targetFormats( TargetFormat.Msi, TargetFormat.Exe, TargetFormat.Deb)

                packageName = "RotaFood Printer"
                packageVersion = project.version as String?
                description   = "Gerenciador de impressão RotaFood"
                vendor        = "RotaFood Ltda."
                copyright     = "© 2025 RotaFood"

                windows {
                    menuGroup        = "RotaFood"
                    shortcut         = true
                    perUserInstall   = true
                    msiPackageVersion = project.version as String
                    exePackageVersion = project.version as String
                    iconFile.set(project.file("resources/iconIco.ico"))
                }
                linux {
                    iconFile.set(project.file("resources/iconPng.png"))
                }
            }
        }
    }
}

tasks.withType<Jar> {
    manifest {
        attributes["Implementation-Version"] = project.version
    }
}

import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

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

        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

        }

    }
}


compose.desktop {
    application {
        mainClass = "br.com.rotafood.MainKt"

        nativeDistributions {
            targetFormats( TargetFormat.Msi, TargetFormat.Exe, TargetFormat.Deb)

            packageName = "RotaFood Printer"
            packageVersion = "1.0.0"
            description   = "Gerenciador de impressão RotaFood"
            vendor        = "RotaFood Ltda."
            copyright     = "© 2025 RotaFood"

            windows {
                upgradeUuid = "d56a3b35-12a3-4bde-9c6b-4f9b2b1f74f1"
                msiPackageVersion = "1.0.0"
                exePackageVersion = "1.0.0"
                iconFile.set(project.file("resources/icon.ico"))
            }
            linux {
                iconFile.set(project.file("resources/icon.png"))
            }
        }
    }
}

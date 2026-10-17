plugins {
    kotlin("multiplatform") version "1.9.10"
}

kotlin {
    jvm()
    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        val jvmMain by getting
        val jsMain by getting
    }
}

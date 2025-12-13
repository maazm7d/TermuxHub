// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Top-level build file
plugins {
    id("com.android.application") version "8.11.1" apply false
    id("com.android.library") version "8.11.1" apply false
    id("org.jetbrains.kotlin.android") version "2.2.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.21" apply false
    id("com.google.devtools.ksp") version "2.2.21-1.0.24" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

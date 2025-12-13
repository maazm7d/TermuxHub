// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Top-level build file
plugins {
    id("com.android.application") version "8.11.1" apply false
    id("com.android.library") version "8.11.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.25" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false  // ★ ADD THIS
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

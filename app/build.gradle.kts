// app/build.gradle.kts
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")   // ★ ADD THIS
}

android {
    namespace = "com.maazm7d.termuxhub"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.maazm7d.termuxhub"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Enable multiDex if you add many dependencies later (optional)
        // multiDexEnabled = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            // Keep default proguard if you add obfuscation rules later
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            // Useful while developing
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
        // You can enable progressive mode if desired:
        // freeCompilerArgs += "-Xprogressive"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        // Compose compiler extension - keep in sync with your Kotlin plugin / Compose BOM
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    packaging {
        resources {
            excludes += setOf("/META-INF/{AL2.0,LGPL2.1}")
        }
    }

    // Optional: enable viewBinding or dataBinding if you plan to use them
    // buildFeatures { viewBinding = true }
}

dependencies {
    // Compose BOM (manage compose versions from BOM)
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))

    // Compose UI
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("com.google.android.material:material:1.13.0")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Navigation for Compose
    implementation("androidx.navigation:navigation-compose:2.8.0")

    // Lifecycle / ViewModel integrations
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Retrofit + Moshi for JSON
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.moshi:moshi:1.15.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.15.0")

    // OkHttp logging (optional but useful)
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Room (local DB)
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // WorkManager (for periodic metadata sync)
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // Image loading (Coil)
    implementation("io.coil-kt:coil-compose:2.5.0")
    
    // splash 
    implementation("androidx.core:core-splashscreen:1.0.1")
    
    // Hilt for Dependency Injection
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-compiler:2.51.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Optional: Material icons
    implementation("androidx.compose.material:material-icons-extended:1.7.0")

    // Optional: Accompanist swipe to refresh (if you plan pull-to-refresh)
    implementation("com.google.accompanist:accompanist-swiperefresh:0.30.1")

    // Testing (optional)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}
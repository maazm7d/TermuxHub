// app/build.gradle.kts
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.compose")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")   
}

android {
    namespace = "com.maazm7d.termuxhub"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.maazm7d.termuxhub"
        minSdk = 25
        targetSdk = 36
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

    kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
      }
    }

    buildFeatures {
        compose = true
    }


    packaging {
        resources {
            excludes += setOf("/META-INF/{AL2.0,LGPL2.1}")
        }
    }

    // Optional: enable viewBinding or dataBinding if you plan to use them
    // buildFeatures { viewBinding = true }
}

hilt {
    enableAggregatingTask = false
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
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.34.0")

    // Navigation for Compose
    implementation("androidx.navigation:navigation-compose:2.8.0")

    implementation("com.squareup.moshi:moshi:1.15.1")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.1")

    // hilt injection
    implementation("androidx.hilt:hilt-work:1.3.0")
    ksp("androidx.hilt:hilt-compiler:1.3.0")
    implementation("androidx.work:work-runtime-ktx:2.9.1")

    //coil 
    implementation("io.coil-kt:coil-compose:2.6.0")

    // Lifecycle / ViewModel integrations
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Retrofit + Moshi for JSON
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.11.0")

    // OkHttp logging (optional but useful)
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Core markdown engine
    implementation("com.mikepenz:multiplatform-markdown-renderer:0.38.1")

    // Material 3 UI (REQUIRED)
    implementation("com.mikepenz:multiplatform-markdown-renderer-m3:0.38.1")

    // OPTIONAL: syntax highlighting
    implementation("com.mikepenz:multiplatform-markdown-renderer-code:0.38.1")

    // OPTIONAL: image loading with Coil 3
    implementation("com.mikepenz:multiplatform-markdown-renderer-coil3:0.38.1")

    // Room (local DB)
    implementation("androidx.room:room-runtime:2.7.0")
    implementation("androidx.room:room-ktx:2.7.0")
    ksp("androidx.room:room-compiler:2.7.0")

    // WorkManager (for periodic metadata sync)
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    
    
    // splash 
    implementation("androidx.core:core-splashscreen:1.0.1")
    
    // Hilt for Dependency Injection
    implementation("com.google.dagger:hilt-android:2.57")
    ksp("com.google.dagger:hilt-compiler:2.57")
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

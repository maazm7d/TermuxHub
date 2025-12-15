// app/build.gradle.kts (FIXED & CLEAN) 
plugins { 
id("com.android.application") 
id("org.jetbrains.kotlin.android") 
id("org.jetbrains.kotlin.plugin.compose") 
id("com.google.devtools.ksp") 
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
}

buildTypes {
    release {
        isMinifyEnabled = true
        proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
        )
    }
    debug {
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

}

hilt { enableAggregatingTask = false }

dependencies {

/* -------------------- Compose -------------------- */
implementation(platform("androidx.compose:compose-bom:2024.02.00"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.ui:ui-tooling-preview")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.material:material-icons-extended")
debugImplementation("androidx.compose.ui:ui-tooling")

implementation("androidx.navigation:navigation-compose:2.8.0")
implementation("com.google.android.material:material:1.13.0")
implementation("com.google.accompanist:accompanist-systemuicontroller:0.34.0")

/* -------------------- Lifecycle -------------------- */
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

/* -------------------- Network -------------------- */
implementation("com.squareup.retrofit2:retrofit:2.11.0")
implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
implementation("com.squareup.retrofit2:converter-scalars:2.11.0")

implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

implementation("com.squareup.moshi:moshi:1.15.1")
implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.1")

/* -------------------- Markdown (Compose RichText) -------------------- */
implementation("com.github.jeziellago:compose-markdown:0.5.8")

/* -------------------- Image Loading -------------------- */
implementation("io.coil-kt:coil-compose:2.6.0")

/* -------------------- Database -------------------- */
implementation("androidx.room:room-runtime:2.7.0")
implementation("androidx.room:room-ktx:2.7.0")
ksp("androidx.room:room-compiler:2.7.0")

/* -------------------- WorkManager -------------------- */
implementation("androidx.work:work-runtime-ktx:2.9.1")

/* -------------------- Hilt -------------------- */
implementation("com.google.dagger:hilt-android:2.57")
ksp("com.google.dagger:hilt-compiler:2.57")
implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
implementation("androidx.hilt:hilt-work:1.3.0")
ksp("androidx.hilt:hilt-compiler:1.3.0")

/* -------------------- Splash -------------------- */
implementation("androidx.core:core-splashscreen:1.0.1")

/* -------------------- Testing -------------------- */
testImplementation("junit:junit:4.13.2")
androidTestImplementation("androidx.test.ext:junit:1.1.5")
androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.00"))
androidTestImplementation("androidx.compose.ui:ui-test-junit4")

}

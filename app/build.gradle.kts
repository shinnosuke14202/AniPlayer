plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.aniplayer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.aniplayer"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.photoview)

    // coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.media)
    ksp(libs.androidx.room.compiler)

    // retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.gson)

    // okhttp
    implementation(libs.logging.interceptor)
    implementation(libs.okhttp)
    implementation(libs.okio)

    // viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // coroutines
    implementation(libs.kotlinx.coroutines.android)

    // hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    // jsoup: HTML parser
    implementation(libs.jsoup)

    // paging
    implementation(libs.androidx.paging.runtime)

    // data serialization (JSON, protobuf, xml)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}

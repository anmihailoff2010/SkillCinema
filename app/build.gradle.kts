plugins {
    id ("com.android.application")
    id ("org.jetbrains.kotlin.android")
    id ("com.google.devtools.ksp")
    id ("dagger.hilt.android.plugin")
    id ("kotlin-parcelize")
    id ("androidx.navigation.safeargs.kotlin")
    id ("com.google.gms.google-services")
    id ("com.google.firebase.crashlytics")
    id ("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.example.skillcinema"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.skillcinema"
        minSdk = 24
        targetSdk = 34
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
    implementation (libs.androidx.core.ktx)
    implementation (libs.androidx.appcompat)
    implementation (libs.material)
    implementation (libs.androidx.constraintlayout)

    // Fragment
    implementation (libs.androidx.fragment.ktx)

    // Navigation
    implementation (libs.androidx.navigation.fragment.ktx)
    implementation (libs.androidx.navigation.ui.ktx)

    // ViewModel
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.runtime.ktx)

    // Retrofit
    implementation (libs.retrofit)

    // Moshi
    implementation (libs.moshi)
    implementation (libs.converter.moshi)
    ksp (libs.moshi.kotlin.codegen)

    // RecyclerView
    implementation (libs.androidx.recyclerview)

    // Paging
    implementation (libs.androidx.paging.runtime.ktx)
    implementation (libs.androidx.swiperefreshlayout)

    // Glide
    implementation (libs.glide)

    // Hilt
    implementation (libs.hilt.android)
    kapt (libs.hilt.android.compiler)

    // Room
    implementation (libs.androidx.room.ktx)
    implementation (libs.androidx.room.runtime)
    implementation (libs.androidx.room.paging)
    ksp (libs.androidx.room.compiler)

    // Firebase
    implementation (platform(libs.firebase.bom))
    implementation (libs.google.firebase.crashlytics.ktx)
    implementation (libs.firebase.analytics.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
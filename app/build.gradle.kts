plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.onekonekmobileapplicationfinal"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.onekonekmobileapplicationfinal"
        minSdk = 22
        targetSdk = 34
        versionCode = 1
        versionName = "5.6.3"

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

    android {
        buildFeatures {
            viewBinding = true
        }
    }



    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation (libs.material.v100)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("com.squareup.okhttp3:okhttp:4.12.0")
}
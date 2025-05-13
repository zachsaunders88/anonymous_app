plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.imagegenerationapp_prototypev1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.imagegenerationapp_prototypev1"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
        //unitTests.isEnabled = false // Temporarily disable to bypass test issues
    }
}

dependencies {
    implementation(libs.okhttp)
    implementation(libs.glide)
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.espresso)
}
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)

    id ("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.practise"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.practise"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Coil
    implementation ("io.coil-kt:coil-compose:2.7.0")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.2.1")

    // Material Icons
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")


    //Dagger - Hilt
    implementation ("com.google.dagger:hilt-android:2.60.1")
    implementation ("androidx.hilt:hilt-navigation-compose:1.2.0")

    ksp("androidx.hilt:hilt-compiler:1.4.0")
    ksp("com.google.dagger:hilt-compiler:2.56.2")


    //Room Database
    implementation ("androidx.room:room-runtime:2.8.5")
    ksp ("androidx.room:room-compiler:2.8.5")
    implementation ("androidx.room:room-ktx:2.8.5")


    //Retrofit2
    implementation ("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation ("com.squareup.retrofit2:retrofit:3.0.0")
    implementation ("com.squareup.okhttp3:okhttp:5.5.0")
    implementation ("com.squareup.retrofit2:converter-scalars:3.0.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.5.0")



}
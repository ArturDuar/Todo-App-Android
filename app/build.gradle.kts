plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.kapt")              // para @Kapt
    id("org.jetbrains.kotlin.plugin.parcelize")  // para @Parcelize
}

android {
    namespace = "edu.udb.todo_app_android"
    compileSdk = 36

    defaultConfig {
        applicationId = "edu.udb.todo_app_android"
        minSdk = 22
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // tus libs via version catalog
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // GSON
    implementation("com.google.code.gson:gson:2.10.1")

    // ROOM
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")          // para Kotlin
    annotationProcessor("androidx.room:room-compiler:$roomVersion") // si tienes código Java
    implementation("androidx.room:room-ktx:$roomVersion")     // extensiones Kotlin
    testImplementation("androidx.room:room-testing:$roomVersion")
}

plugins {
    id("com.android.application")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.smartteamdailyapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.smartteamdailyapplication"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
//    buildFeatures {
//        viewBinding {
//            enabled = true
//        }
//    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("javax.inject:javax.inject:1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.room:room-common:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.google.dagger:hilt-android:2.38.1")
    annotationProcessor("com.google.dagger:hilt-compiler:2.38.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")

}

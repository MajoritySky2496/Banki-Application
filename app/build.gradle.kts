plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.example.bankiapplication"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.dostoin.zaem.app"
        minSdk = 21
        targetSdk = 33
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

}


dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")
    implementation("com.google.firebase:firebase-firestore-ktx:24.10.0")
    implementation("com.google.android.gms:play-services-ads-identifier:18.0.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    //Koin
    implementation("io.insert-koin:koin-android:3.4.2")

    //FireBase
    implementation(platform("com.google.firebase:firebase-bom:32.5.0"))
    implementation("com.google.firebase:firebase-messaging-ktx:23.4.0")
    implementation ("com.google.firebase:firebase-messaging:23.4.0")
    implementation ("com.google.android.gms:play-services-base:18.2.0")

    //RxPermission
    implementation ("com.github.tbruyelle:rxpermissions:0.12")
    implementation ("io.reactivex.rxjava3:rxjava:3.0.4")

    //Gif
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.22")

    //AppMetrica SDK
    implementation ("com.yandex.android:mobmetricalib:5.3.0")
    implementation ("com.yandex.android:mobmetricapushlib:2.3.3")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")


    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))

    // Add the dependencies for the Crashlytics and Analytics libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-crashlytics")

    //Facebook
    implementation ("com.facebook.android:facebook-android-sdk:latest.release")

    //Сoroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")


    //AdvId



}
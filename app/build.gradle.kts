plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs")
}

@Suppress("UnstableApiUsage")
android {

    buildFeatures {
        dataBinding = true
    }

    compileSdk = 33

    defaultConfig {
        applicationId = "com.yassineabou.calculator"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    namespace = "com.yassineabou.calculator"
}

dependencies {

    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.core:core-ktx:1.10.1")
    val navVersion = "2.6.0"
    val roomVersion = "2.5.2"
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // sdp
    implementation("com.intuit.sdp:sdp-android:1.1.0")
    // ssp
    implementation("com.intuit.ssp:ssp-android:1.1.0")
    // mXparser
    implementation("org.mariuszgromada.math:MathParser.org-mXparser:5.2.1")
    // navigation
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
    // room
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    // hilt
    implementation("com.google.dagger:hilt-android:2.47")
    kapt("com.google.dagger:hilt-compiler:2.47")
}
android {
    buildTypes {
        create("benchmark") {
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }
    defaultConfig {
        signingConfig = signingConfigs.getByName("debug")
    }
}

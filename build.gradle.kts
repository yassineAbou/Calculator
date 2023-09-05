// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val kotlinVersion by rootProject.extra { "1.8.21" }

    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.47")
        val navVersion = "2.6.0"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
    repositories {
        mavenCentral()
        google()
    }
}

plugins {
    id("com.android.application") version "8.0.2" apply false
    id("com.android.library") version "8.0.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.21" apply false
    id("com.android.test") version "8.0.2" apply false
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}

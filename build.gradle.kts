// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val kotlinVersion by rootProject.extra { "2.2.0" }

    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.56.2")
        val navVersion = "2.9.2"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
    repositories {
        mavenCentral()
        google()
    }
}

plugins {
    id("com.android.application") version "8.11.1" apply false
    id("com.android.library") version "8.11.1" apply false
    id("org.jetbrains.kotlin.android") version "2.2.0" apply false
    id("com.android.test") version "8.11.1" apply false
    id("com.google.devtools.ksp") version "2.2.0-2.0.2" apply false


}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}

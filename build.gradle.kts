// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val kotlinVersion by rootProject.extra { "1.9.22" }

    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
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
    id("com.android.application") version "8.2.2" apply false
    id("com.android.library") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.android.test") version "8.2.2" apply false
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false


}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}

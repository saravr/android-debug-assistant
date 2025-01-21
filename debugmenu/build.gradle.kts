plugins {
    id("com.android.library")
    kotlin("android")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("maven-publish")
    id("com.sandymist.mobile.plugin.interceptor")

    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.sandymist.android.debugmenu"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = true
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro", "consumer-rules.pro"))
        }
        named("debug") {
        }
    }

    sourceSets {
        all {
            java {
                setSrcDirs(listOf("src/main/kotlin"))
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
        allWarningsAsErrors = true
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // dagger/hilt
    implementation(libs.hilt.android)
    debugImplementation(libs.ui.tooling)
    ksp(libs.hilt.android.compiler)

    // retrofit
    implementation(libs.retrofit)

    // serialization
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.kotlinx.serialization.json)

    // room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    implementation(libs.timber)
    implementation(libs.android.utilities)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

configure<PublishingExtension> {
    publications.create<MavenPublication>("debug") {
        groupId = "com.sandymist.android"
        artifactId = "debugmenu"
        version = rootProject.extra["projectVersion"] as String
        afterEvaluate {
            from(components["debug"])
        }
    }

    repositories {
        mavenLocal()
    }
}

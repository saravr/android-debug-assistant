plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("maven-publish")
}

android {
    namespace = "com.sandymist.android.debugmenu"
    compileSdk = 35

    defaultConfig {
        minSdk = 25

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
        maybeCreate("qa").apply {
            initWith(getByName("debug")) // Optional: inherit configurations from debug
        }
    }

    sourceSets {
//        // Configure the main source set (common for all)
//        main {
//            java.srcDirs("src/main/kotlin")
//            // Add other necessary directories like resources, etc.
//        }

        // Configure the debug source set
        getByName("debug") {
            java.srcDirs("src/debug/kotlin")
            // You can also add additional directories like "src/debug/java" if needed
        }

        // Configure the release source set
        getByName("release") {
            java.srcDirs("src/release/kotlin")
            // You can customize release-specific source directories here
        }

        // Configure the qa source set
        getByName("qa") {
            java.srcDirs("src/debug/kotlin") // Use src/qa/kotlin for QA build type
            // If QA should use the same resources as Debug, you can add that as well:
            res.srcDirs("src/debug/res")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
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
    implementation(libs.androidx.hilt.navigation.compose)
    debugImplementation(libs.androidx.ui.tooling)
    kapt(libs.hilt.android.compiler)

    // retrofit
    implementation(libs.retrofit)

    // serialization
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.kotlinx.serialization.json)

    // room
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
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
        artifactId = "debugmenu-debug"
        version = rootProject.extra["projectVersion"] as String
        afterEvaluate {
            from(components["debug"])
        }
    }

    publications.create<MavenPublication>("qa") {
        groupId = "com.sandymist.android"
        artifactId = "debugmenu-qa"
        version = rootProject.extra["projectVersion"] as String
        afterEvaluate {
            from(components["qa"])
        }
    }

    publications.create<MavenPublication>("no-op") {
        groupId = "com.sandymist.android"
        artifactId = "debugmenu-no-op"
        version = rootProject.extra["projectVersion"] as String
        afterEvaluate {
            from(components["release"])
        }
    }

    repositories {
        mavenLocal()
    }
}

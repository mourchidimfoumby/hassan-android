plugins {
    alias(libs.plugins.android.library)
}

val ORACLE_BUCKET_URL: String =
    project.findProperty("ORACLE_BUCKET_URL") as String?
        ?: System.getenv("ORACLE_BUCKET_URL")
        ?: error("ORACLE_BUCKET_URL is not set in gradle.properties or as environment variable")

android {
    namespace = "com.mfoumby.hassan.common.data"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField(
            "String",
            "ORACLE_BUCKET_URL",
            "\"$ORACLE_BUCKET_URL\"",
        )
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
    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.koin.core)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.firestore)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(project(":common:domain"))
}
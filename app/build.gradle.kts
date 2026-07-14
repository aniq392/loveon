import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

// ⚠️ 기존에 여기에 있던 val localProperties 코드를 android 블록 안쪽으로 옮겼습니다!

android {
    namespace = "xyz.fsg123.loveon"

    // 1. local.properties 파일 읽어오기 설정을 이 위치로 이동
    val localProperties = Properties().apply {
        val file = rootProject.file("local.properties")
        if (file.exists()) {
            file.inputStream().use { load(it) }
        }
    }

    // 2026년 기준 Android 16 (API 36) 설정 유지
    compileSdk = 36

    defaultConfig {
        applicationId = "xyz.fsg123.loveon"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // ⭐ local.properties에서 값을 읽어 BuildConfig 변수로 주입합니다.
        val kakaoKey = localProperties.getProperty("KAKAO_NATIVE_APP_KEY") ?: ""
        val naverId = localProperties.getProperty("NAVER_CLIENT_ID") ?: ""
        val naverSecret = localProperties.getProperty("NAVER_CLIENT_SECRET") ?: ""
        val googleClientId = localProperties.getProperty("GOOGLE_WEB_CLIENT_ID") ?: ""

        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"$kakaoKey\"")
        buildConfigField("String", "NAVER_CLIENT_ID", "\"$naverId\"")
        buildConfigField("String", "NAVER_CLIENT_SECRET", "\"$naverSecret\"")
        buildConfigField("String", "GOOGLE_WEB_CLIENT_ID", "\"$googleClientId\"")
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
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    testImplementation("androidx.test:core:1.6.1")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("androidx.navigation:navigation-compose:2.8.5")
    implementation("androidx.compose.material:material-icons-extended:1.7.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.google.dagger:hilt-android:2.51.1")

    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation("com.kakao.sdk:v2-user:2.24.0")
    implementation("com.navercorp.nid:oauth:5.10.0")
    implementation(platform("com.google.firebase:firebase-bom:34.15.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("androidx.appcompat:appcompat:1.6.1") // 혹은 최신 버전
}
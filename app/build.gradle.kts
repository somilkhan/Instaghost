plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}
android {
    namespace = "dev.instaghost.module"
    compileSdk = 35
    defaultConfig {
        applicationId = "dev.instaghost.module"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0"
    }
    buildTypes { release { isMinifyEnabled = true; proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro") } }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    kotlinOptions { jvmTarget = "17" }
}
dependencies {
    compileOnly("de.robv.android.xposed:api:82")   // classic API: works with LSPosed
    implementation("org.luckypray:dexkit:2.0.2")   // finds obfuscated classes by strings; verify latest version\n    testImplementation("junit:junit:4.13.2")
}

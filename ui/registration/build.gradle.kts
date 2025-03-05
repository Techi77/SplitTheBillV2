plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.stb.registration"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
}
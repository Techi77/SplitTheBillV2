plugins {
    id("stb.corePlugin")
}
android {
    namespace = "com.stb.core.appBase"
}
dependencies {
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.android)
}

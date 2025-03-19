plugins {
    id("stb.uiPlugin")
    id("stb.firebasePlugin")
}

android {
    namespace = "com.stb.ui.registration"
}

dependencies {
    implementation(project(":core:theme"))
    implementation(project(":core:components"))
}
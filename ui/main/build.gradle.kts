plugins {
    id("stb.uiPlugin")
}

android {
    namespace = "com.stb.ui.main"
}

dependencies {
    implementation(project(":core:theme"))
    implementation(project(":core:components"))
    implementation(project(":data:preferences"))
}
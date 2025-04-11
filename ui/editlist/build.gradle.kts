plugins {
    id("stb.uiPlugin")
}

android {
    namespace = "com.stb.ui.editlist"
}

dependencies {
    implementation(project(":core:theme"))
    implementation(project(":core:components"))
}
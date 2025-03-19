plugins {
    id("stb.uiPlugin")
}

android {
    namespace = "com.stb.components"
}
dependencies {
    implementation(project(":core:theme"))
}

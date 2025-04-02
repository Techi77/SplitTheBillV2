plugins {
    id("stb.dataPlugin")
    id("stb.appBasePlugin")
}
android {
    namespace = "com.stb.data.preferences"
}

dependencies{
    implementation(libs.datastore.preferences)
}
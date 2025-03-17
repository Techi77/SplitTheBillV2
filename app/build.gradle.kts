plugins {
    id("stb.appPlugin")
}

android {
    namespace = "com.stb.splitthebill" /*это всегда остаётся*/
}
dependencies {
    implementation(project(":ui:registration"))
    implementation(project(":core:theme"))
    implementation(project(":core:theme"))
}

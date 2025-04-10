plugins {
    id("stb.appPlugin")
    id("stb.firebasePlugin")
}

android {
    namespace = "com.stb.splitthebill" /*это всегда остаётся*/
}
dependencies {
    implementation(project(":ui:registration"))
    implementation(project(":ui:main"))
    implementation(project(":ui:editlist"))
    implementation(project(":core:theme"))
    implementation(project(":data:preferences"))
}

plugins {
    id("stb.appPlugin")
}

android {
    namespace = "com.stb.splitthebill" /*это всегда остаётся*/
    dynamicFeatures += setOf(":ui:registration")
}
object GradleConstants {
    object SDK {
        const val COMPILE_SDK = 35
        const val MIN_SDK = 24
        const val TARGET_SDK = 35
    }

    object Plugins {
        const val ANDROID_APPLICATION = "com.android.application"
        const val KOTLIN_ANDROID = "org.jetbrains.kotlin.android"
        const val ANDROID_LIBRARY = "com.android.library"
        const val KOTLIN_PARCELIZE = "org.jetbrains.kotlin.plugin.parcelize"
        const val KOTLIN_SERIALIZATION = "org.jetbrains.kotlin.plugin.serialization"
        const val KOTLIN_COMPOSE = "org.jetbrains.kotlin.plugin.compose"
        const val GOOGLE_SERVICES = "com.google.gms.google-services"
        const val KOTLIN_KSP = "com.google.devtools.ksp"
        const val HILT = "com.google.dagger.hilt.android"
    }

    object Libs {
        const val ANDROID_J_UNIT_RUNNER = "androidx.test.runner.AndroidJUnitRunner"
    }

    object Version {
        const val CODE = 1
        const val NAME = "1.0.0"
    }

    const val APP_ID = "com.stb.splitthebill"
}
package extentions

import com.android.build.gradle.AppExtension
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun Project.configureAndroidModule() = extensions.configure<AppExtension> {
    compileSdkVersion(GradleConstants.SDK.COMPILE_SDK)
    defaultConfig {
        applicationId = GradleConstants.APP_ID
        minSdk = GradleConstants.SDK.MIN_SDK
        targetSdk = GradleConstants.SDK.TARGET_SDK
        versionCode = GradleConstants.Version.CODE
        versionName = GradleConstants.Version.NAME

        testInstrumentationRunner = GradleConstants.Libs.ANDROID_J_UNIT_RUNNER
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(17)
    }
}

private fun Project.kotlin(configure: Action<KotlinAndroidProjectExtension>): Unit =
    (this as ExtensionAware).extensions.configure("kotlin", configure)
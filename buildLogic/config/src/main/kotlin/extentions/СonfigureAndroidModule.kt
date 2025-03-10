package extentions

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal fun configureAndroidModule(
    commonExtension: CommonExtension<*, *, *, *, *>,
    project: Project
) {
    commonExtension.compileSdk = GradleConstants.SDK.COMPILE_SDK
    commonExtension.defaultConfig {
        minSdk = GradleConstants.SDK.MIN_SDK
        testInstrumentationRunner = GradleConstants.Libs.ANDROID_J_UNIT_RUNNER
    }
    commonExtension.compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    project.kotlin {
        jvmToolchain(17)
    }
}

private fun Project.kotlin(configure: Action<KotlinAndroidProjectExtension>): Unit =
    (this as ExtensionAware).extensions.configure("kotlin", configure)
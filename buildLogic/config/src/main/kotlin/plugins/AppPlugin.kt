package plugins

import GradleConstants
import com.android.build.api.dsl.ApplicationExtension
import extentions.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AppPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(plugins) {
                apply(GradleConstants.Plugins.ANDROID_APPLICATION)
                apply(GradleConstants.Plugins.KOTLIN_ANDROID)
                apply(GradleConstants.Plugins.KOTLIN_SERIALIZATION)
                apply(GradleConstants.Plugins.GOOGLE_SERVICES)
            }

            extensions.configure<ApplicationExtension> {
                defaultConfig {
                    applicationId = GradleConstants.APP_ID
                }
                addCompose(this)
                addUiHilt(this)
                configureAndroidModule(this, project)
                defaultConfig {
                    targetSdk = GradleConstants.SDK.TARGET_SDK
                    versionCode = GradleConstants.Version.CODE
                    versionName = GradleConstants.Version.NAME
                }
            }

            val libs = libs()
            dependencies {
                add("implementation", libs.coreKtx())
                add("implementation", libs.lifecycleRuntimeKtx())
                add("implementation", libs.kotlinxSerialization())

                add("androidTestImplementation", libs.uiTestJunit4())
                add("debugImplementation", libs.uiTestManifest())
                add("testImplementation", libs.junit())
                add("androidTestImplementation", libs.androidxJunit())
                add("androidTestImplementation", libs.espressoCore())
                add("androidTestImplementation", platform(libs.composeBom()))
            }
        }
    }
}


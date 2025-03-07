package plugins

import GradleConstants
import com.android.build.api.dsl.ApplicationExtension
import extentions.addCompose
import extentions.configureAndroidModule
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AppPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(plugins) {
                apply(GradleConstants.Libs.ANDROID_APPLICATION)
                apply(GradleConstants.Libs.KOTLIN_ANDROID)
            }

            extensions.configure<ApplicationExtension> {
                addCompose(this)
            }

            configureAndroidModule()
        }
    }
}


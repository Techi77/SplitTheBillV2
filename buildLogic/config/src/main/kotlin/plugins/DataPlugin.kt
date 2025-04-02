package plugins

import com.android.build.api.dsl.LibraryExtension
import extentions.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class DataPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(plugins) {
                apply(GradleConstants.Plugins.ANDROID_LIBRARY)
                apply(GradleConstants.Plugins.KOTLIN_ANDROID)
                apply(GradleConstants.Plugins.KOTLIN_SERIALIZATION)
            }

            extensions.configure<LibraryExtension> {
                configureAndroidModule(this, project)
            }

            val libs = libs()
            dependencies{
                add("implementation", libs.kotlinxSerialization())
            }
        }
    }
}
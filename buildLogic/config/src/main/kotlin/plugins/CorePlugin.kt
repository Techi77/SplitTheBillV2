package plugins

import com.android.build.api.dsl.LibraryExtension
import extentions.addUiHilt
import extentions.configureAndroidModule
import extentions.coreKtx
import extentions.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class CorePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(plugins) {
                apply(GradleConstants.Plugins.ANDROID_LIBRARY)
                apply(GradleConstants.Plugins.KOTLIN_ANDROID)
            }

            extensions.configure<LibraryExtension> {
                configureAndroidModule(this, project)
                addUiHilt(this)
            }

            val libs = libs()
            dependencies {
                add("implementation", libs.coreKtx())
            }
        }
    }
}
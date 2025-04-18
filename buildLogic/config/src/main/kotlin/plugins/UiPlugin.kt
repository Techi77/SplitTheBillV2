package plugins

import com.android.build.api.dsl.LibraryExtension
import extentions.addCompose
import extentions.addUiHilt
import extentions.configureAndroidModule
import extentions.coreKtx
import extentions.libs
import extentions.material
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class UiPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            with(plugins) {
                apply(GradleConstants.Plugins.ANDROID_LIBRARY)
                apply(GradleConstants.Plugins.KOTLIN_ANDROID)
            }

            extensions.configure<LibraryExtension> {
                addCompose(this)
                addUiHilt(this)
                configureAndroidModule(this, project)
            }

            val libs = libs()
            dependencies {
                add("implementation", libs.coreKtx())
                add("implementation", libs.material())
                add("implementation", project(":core:feature:appBase"))
            }
        }
    }
}
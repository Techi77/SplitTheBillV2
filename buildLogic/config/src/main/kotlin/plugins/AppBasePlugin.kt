package plugins

import com.android.build.api.dsl.LibraryExtension
import extentions.addCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AppBasePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            extensions.configure<LibraryExtension> {
                addCompose(this)
            }
        }
    }
}
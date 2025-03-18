package plugins

import extentions.firebaseAnalytics
import extentions.firebaseAuth
import extentions.firebaseBom
import extentions.googleFirebaseAuth
import extentions.libs
import extentions.playServicesAuth
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class FirebasePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            val libs = libs()
            dependencies {
                add("implementation", platform(libs.firebaseBom()))
                add("implementation", libs.firebaseAuth())
                add("implementation", libs.firebaseAnalytics())
                add("implementation", libs.googleFirebaseAuth())
                add("implementation", libs.playServicesAuth())
            }
        }
    }
}
package extentions

import GradleConstants
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.addCompose(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    val libs = libs()

    commonExtension.apply {
        pluginManager.apply(GradleConstants.Libs.KOTLIN_COMPOSE)
        buildFeatures {
            compose = true
        }
        dependencies {
            val bom = libs.composeBom()
            add("implementation", platform(bom))
            add("androidTestImplementation", platform(bom))
            add("implementation", libs.composeRuntime())
            add("implementation", libs.activityCompose())
            add("implementation", libs.ui())
            add("implementation", libs.uiGraphics())
            add("implementation", libs.uiToolingPreview())
            add("implementation", libs.material3())
        }
    }
}
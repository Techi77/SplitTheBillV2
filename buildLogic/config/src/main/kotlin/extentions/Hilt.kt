package extentions

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.addHilt(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    commonExtension.apply {
        pluginManager.apply(GradleConstants.Plugins.KOTLIN_KSP)
    }

    val libs = libs()
    dependencies {
        add("implementation", libs.hiltAndroid())
        add("ksp", libs.hiltAndroidCompiler())
    }
}

internal fun Project.addUiHilt(
    commonExtension: CommonExtension<*, *, *, *, *>,
) {
    addHilt(commonExtension)
    commonExtension.apply {
        pluginManager.apply(GradleConstants.Plugins.HILT)
    }
    val libs = libs()
    dependencies{
        add("implementation", libs.hiltCompose())
    }
}
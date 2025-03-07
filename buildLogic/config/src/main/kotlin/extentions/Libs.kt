package extentions

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

internal fun Project.libs() = extensions.getByType<VersionCatalogsExtension>().named("libs")

// Compose
internal fun VersionCatalog.composeBom() = findLibrary("androidx-compose-bom").get()
internal fun VersionCatalog.composeRuntime() = findLibrary("androidx-compose-runtime").get()
internal fun VersionCatalog.activityCompose() = findLibrary("androidx-activity-compose").get()
internal fun VersionCatalog.navigationCompose() = findLibrary("androidx-navigation-compose").get()

internal fun VersionCatalog.ui() = findLibrary("androidx-ui").get()
internal fun VersionCatalog.uiGraphics() = findLibrary("androidx-ui-graphics").get()
internal fun VersionCatalog.uiTooling() = findLibrary("androidx-ui-tooling").get()
internal fun VersionCatalog.uiToolingPreview() = findLibrary("androidx-ui-tooling-preview").get()

internal fun VersionCatalog.coreKtx() = findLibrary("androidx-core-ktx").get()
internal fun VersionCatalog.lifecycleRuntimeKtx() = findLibrary("androidx-lifecycle-runtime-ktx").get()

internal fun VersionCatalog.material3() = findLibrary("androidx-material3").get()

// Test
internal fun VersionCatalog.uiTestJunit4() = findLibrary("androidx-ui-test-junit4").get()
internal fun VersionCatalog.uiTestManifest() = findLibrary("androidx-ui-test-manifest").get()
internal fun VersionCatalog.junit() = findLibrary("junit").get()
internal fun VersionCatalog.androidxJunit() = findLibrary("androidx-junit").get()
internal fun VersionCatalog.espressoCore() = findLibrary("androidx-espresso-core").get()

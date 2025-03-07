package extentions

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType

internal fun Project.libs() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun VersionCatalog.composeBom() = findLibrary("androidx-compose-bom").get()
internal fun VersionCatalog.composeRuntime() = findLibrary("androidx-compose-runtime").get()
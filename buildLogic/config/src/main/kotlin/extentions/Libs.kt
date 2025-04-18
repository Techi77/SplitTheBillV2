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

internal fun VersionCatalog.kotlinxSerialization() = findLibrary("kotlinx-serialization-json").get()

internal fun VersionCatalog.material3() = findLibrary("androidx-material3").get()
internal fun VersionCatalog.material() = findLibrary("material").get()

// Test
internal fun VersionCatalog.uiTestJunit4() = findLibrary("androidx-ui-test-junit4").get()
internal fun VersionCatalog.uiTestManifest() = findLibrary("androidx-ui-test-manifest").get()
internal fun VersionCatalog.junit() = findLibrary("junit").get()
internal fun VersionCatalog.androidxJunit() = findLibrary("androidx-junit").get()
internal fun VersionCatalog.espressoCore() = findLibrary("androidx-espresso-core").get()

// Firebase
internal fun VersionCatalog.firebaseBom() = findLibrary("firebase-bom").get()
internal fun VersionCatalog.firebaseAuth() = findLibrary("firebase-auth").get()
internal fun VersionCatalog.firebaseAnalytics() = findLibrary("firebase-analytics").get()
internal fun VersionCatalog.credentials() = findLibrary("credentials").get()
internal fun VersionCatalog.credentialsPlayServicesAuth() = findLibrary("credentials-play-services-auth").get()
internal fun VersionCatalog.googleId() = findLibrary("googleid").get()

//google auth
internal fun VersionCatalog.googleFirebaseAuth() = findLibrary("google-firebase-auth").get()
internal fun VersionCatalog.playServicesAuth() = findLibrary("play-services-auth").get()

//hilt
internal fun VersionCatalog.hiltAndroid() = findLibrary("hilt-android").get()
internal fun VersionCatalog.hiltAndroidCompiler() = findLibrary("hilt-android-compiler").get()
internal fun VersionCatalog.hiltCompose() = findLibrary("hilt-compose").get()
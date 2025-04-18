import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "com.stb.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies{
    compileOnly(libs.gradle)
    compileOnly(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("emptyPlugin") {
            id = "stb.emptyPlugin"
            implementationClass = "plugins.EmptyPlugin"
        }
    }
    plugins {
        register("appPlugin") {
            id = "stb.appPlugin"
            implementationClass = "plugins.AppPlugin"
        }
    }
    plugins {
        register("uiPlugin") {
            id = "stb.uiPlugin"
            implementationClass = "plugins.UiPlugin"
        }
    }
    plugins {
        register("corePlugin") {
            id = "stb.corePlugin"
            implementationClass = "plugins.CorePlugin"
        }
    }
    plugins {
        register("firebasePlugin") {
            id = "stb.firebasePlugin"
            implementationClass = "plugins.FirebasePlugin"
        }
    }
    plugins {
        register("appBasePlugin") {
            id = "stb.appBasePlugin"
            implementationClass = "plugins.AppBasePlugin"
        }
    }
    plugins {
        register("dataPlugin") {
            id = "stb.dataPlugin"
            implementationClass = "plugins.DataPlugin"
        }
    }
}